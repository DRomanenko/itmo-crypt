import java.io.*
import java.security.MessageDigest
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.pow

class MerkleTree internal constructor(private val height: Int, indexes: IntArray, values: Array<String?>) {
    private val encoder = Base64.getEncoder()
    private val decoder = Base64.getDecoder()
    private var nodes: MutableMap<Int, Map<Int, String?>> = HashMap()
    private var nodesId: IntArray = IntArray(height)
    private var nodesByIndex: MutableMap<Int, String?> = HashMap()
    private val digest: MessageDigest = MessageDigest.getInstance("SHA-256")

    init {
        val nodesMap: MutableMap<Int, String?> = HashMap()
        for (i in indexes.indices) {
            nodesByIndex[indexes[i]] = values[i]
            nodesMap[indexes[i]] = String(enc(digest.digest(concat(byteArrayOf(0), dec(values[i])))))
        }
        nodes[height] = nodesMap
        setupNodes(height, 0)
    }

    private fun setupNodes(height: Int, start: Int) {
        if (height == 0) return
        val curMap = nodes[height]!!
        val pow = 2.0.pow(height).toInt()
        val nextMap: MutableMap<Int, String?> = HashMap()
        for ((id, dataHash) in curMap) {
            var newId = checkId(id)
            val parentHash = if (curMap.containsKey(newId)) curMap[newId] else null
            newId = pow + id - id % 2 - (id - start) / 2
            nextMap[newId] = if (id % 2 == 0) sha256(dataHash, parentHash) else sha256(parentHash, dataHash)
        }
        nodes[height - 1] = nextMap
        setupNodes(height - 1, start + pow)
    }

    private fun concat(left: ByteArray, right: ByteArray): ByteArray {
        val result = ByteArray(left.size + right.size)
        System.arraycopy(left, 0, result, 0, left.size)
        System.arraycopy(right, 0, result, left.size, right.size + left.size - left.size)
        return result
    }

    private fun setupNodesId(id: Int, height: Int, start: Int, i: Int) {
        if (height == 0) return
        nodesId[i] = id
        val pow = 2.0.pow(height).toInt()
        val newId = pow + id - id % 2 - (id - start) / 2
        setupNodesId(newId, height - 1, start + pow, i + 1)
    }

    private fun sha256(left: String?, right: String?): String? {
        if (left == null && right == null)
            return null
        val l = dec(left ?: "")
        val r = dec(right ?: "")
        val sha256 = digest.digest(concat(concat(concat(byteArrayOf(1), l), byteArrayOf(2)), r))
        return String(enc(sha256))
    }

    private fun enc(byteArray: ByteArray): ByteArray {
        return encoder.encode(byteArray)
    }

    private fun dec(string: String?): ByteArray {
        return decoder.decode(string)
    }

    fun print(indexes: IntArray) {
        try {
            BufferedWriter(OutputStreamWriter(System.out)).use { writer ->
                for (index in indexes) {
                    writer.write(index.toString() + " " + nodesByIndex[index])
                    writer.newLine()
                    setupNodesId(index, height, 0, 0)
                    for (i in 0 until height) {
                        writer.write(nodes[height - i]!![checkId(nodesId[i])] ?: "null")
                        writer.newLine()
                    }
                }
            }
        } catch (e: IOException) {
            System.err.println("ERROR: Output - " + e.localizedMessage)
        }
    }

    private fun checkId(id: Int) = id + if (id % 2 == 0) 1 else -1
}

fun main() {
    try {
        BufferedReader(InputStreamReader(System.`in`)).use { reader ->
            val height = reader.readLine().toInt()
            val n = reader.readLine().toInt()
            var indexes = IntArray(n)
            val dataHash = arrayOfNulls<String>(n)
            var blocks: Array<String>
            for (i in 0 until n) {
                blocks = reader.readLine().split(" ".toRegex()).toTypedArray()
                indexes[i] = blocks[0].toInt()
                dataHash[i] = blocks[1]
            }
            val merkleTree = MerkleTree(height, indexes, dataHash)

            val q = reader.readLine().toInt()
            indexes = IntArray(q)
            blocks = reader.readLine().split(" ".toRegex()).toTypedArray()
            for (i in 0 until q) {
                indexes[i] = blocks[i].toInt()
            }
            merkleTree.print(indexes)
        }
    } catch (e: IOException) {
        System.err.println("ERROR: Input - " + e.localizedMessage)
    }
}
