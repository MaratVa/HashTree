package com.example.treetest

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class NodeSerializable(
    val id: String,
    val children: List<NodeSerializable> = emptyList()
)

class TreeViewModel(private val treeStorage: TreeStorage) : ViewModel() {

    private val _currentNode = MutableStateFlow<Node?>(null)
    val currentNode = _currentNode.asStateFlow()

    private val _rootNode = MutableStateFlow<Node>(Node())
    val rootNode = _rootNode.asStateFlow()

    private val gson = Gson()

    init {
        viewModelScope.launch {
            treeStorage.treeJsonFlow.collect { jsonString ->
                try {
                    if (!jsonString.isNullOrEmpty()) {
                        val nodeType = object : TypeToken<NodeSerializable>() {}.type
                        val nodeSerializable: NodeSerializable = gson.fromJson(jsonString, nodeType)
                        val root = fromSerializable(nodeSerializable)
                        _rootNode.value = root
                        _currentNode.value = root
                    } else {

                        val newRoot = Node()
                        _rootNode.value = newRoot
                        _currentNode.value = newRoot
                    }
                } catch (e: Exception) {

                    Log.e("TreeViewModel", "Error parsing JSON", e)

                    val newRoot = Node()
                    _rootNode.value = newRoot
                    _currentNode.value = newRoot
                }
            }
        }
    }

    fun setCurrentNode(node: Node) {
        _currentNode.value = node
    }

    fun addChild(parent: Node) {
        val newChild = Node(parent = parent)
        parent.children.add(newChild)
        updateTreeState(parent)
    }

    fun deleteChild(parent: Node, child: Node) {
        parent.children.remove(child)
        updateTreeState(parent)
    }

    private fun updateTreeState(focusNode: Node) {

        _rootNode.value = copyNode(_rootNode.value)
        _currentNode.value = focusNode
        saveTree()
    }

    private fun copyNode(node: Node): Node {
        val newNode = Node(node.id, node.parent)
        newNode.children.addAll(node.children.map { copyNode(it) })
        return newNode
    }

    private fun saveTree() {
        viewModelScope.launch {
            try {
                val jsonString = gson.toJson(toSerializable(_rootNode.value))
                treeStorage.saveTreeJson(jsonString)
            } catch (e: Exception) {
                Log.e("TreeViewModel", "Error saving tree", e)
            }
        }
    }

    private fun toSerializable(node: Node): NodeSerializable =
        NodeSerializable(
            id = node.id,
            children = node.children.map { toSerializable(it) }
        )

    private fun fromSerializable(nodeS: NodeSerializable, parent: Node? = null): Node {
        val node = Node(nodeS.id, parent)
        node.children.addAll(nodeS.children.map { fromSerializable(it, node) })
        return node
    }
}

