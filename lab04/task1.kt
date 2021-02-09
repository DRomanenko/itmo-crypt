import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.security.MessageDigest
import java.util.*
import kotlin.math.pow

class MerkleTree(private var root: String, private var height: Int) {
    private val encoder = Base64.getEncoder()
    private val decoder = Base64.getDecoder()
    private var nodes: IntArray = IntArray(height)
    private val digest: MessageDigest = MessageDigest.getInstance("SHA-256")

    fun verify(dataBlock: String?, parentsHash: Array<String?>): Boolean {
        var dataHash = if (dataBlock == "null") null else
            String(enc(digest.digest(concat(byteArrayOf(0), dec(dataBlock)))))

        (0 until height).forEach { i ->
            val parentHash = if (parentsHash[i] == "null") null else parentsHash[i]
            dataHash = if (nodes[i] % 2 == 0) sha256(dataHash, parentHash) else sha256(parentHash, dataHash)
        }

        return dataHash != null && dataHash == root
    }

    private fun concat(left: ByteArray, right: ByteArray): ByteArray {
        val result = ByteArray(left.size + right.size)
        System.arraycopy(left, 0, result, 0, left.size)
        System.arraycopy(right, 0, result, left.size, right.size + left.size - left.size)
        return result
    }

    fun setup(id: Int, height: Int, start: Int, i: Int) {
        if (height == 0) return
        nodes[i] = id
        val pow = 2.0.pow(height).toInt()
        val newId = pow + id - id % 2 - (id - start) / 2
        setup(newId, height - 1, start + pow, i + 1)
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
}

fun main() {
    try {
        BufferedReader(InputStreamReader(System.`in`)).use { bufferedReader ->
            val height = bufferedReader.readLine().toInt()
            val root = bufferedReader.readLine()
            val qtyBlocks = bufferedReader.readLine().toInt()
            val merkleTree = MerkleTree(root, height)
            repeat(qtyBlocks) {
                val header = bufferedReader.readLine().split(" ".toRegex()).toTypedArray()
                val id = header[0].toInt()
                val dataBlock = header[1]
                val parentsHash = arrayOfNulls<String>(height)
                (0 until height).forEach { i ->
                    parentsHash[i] = bufferedReader.readLine()
                }
                merkleTree.setup(id, height, 0, 0)
                println(if (merkleTree.verify(dataBlock, parentsHash)) "YES" else "NO")
            }
        }
    } catch (e: IOException) {
        System.err.println("ERROR: I/O - " + e.localizedMessage)
    }
}