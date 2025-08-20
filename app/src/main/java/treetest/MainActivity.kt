package treetest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import com.example.treetest.NodeScreen
import com.example.treetest.TreeStorage
import com.example.treetest.TreeViewModel
import com.example.treetest.TreeViewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var treeStorage: TreeStorage
    private lateinit var viewModelFactory: TreeViewModelFactory
    private lateinit var treeViewModel: TreeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        treeStorage = TreeStorage(applicationContext)
        viewModelFactory = TreeViewModelFactory(treeStorage)
        treeViewModel = ViewModelProvider(this, viewModelFactory)[TreeViewModel::class.java]

        setContent {
            MaterialTheme {
                Surface {
                    val currentNode by treeViewModel.currentNode.collectAsState()

                    currentNode?.let { node ->
                        NodeScreen(
                            node = node,
                            onNavigate = { treeViewModel.setCurrentNode(it) },
                            onAddChild = { treeViewModel.addChild(it) },
                            onDeleteChild = { parent, child ->
                                treeViewModel.deleteChild(parent, child)
                            }
                        )
                    }
                }
            }
        }
    }
}