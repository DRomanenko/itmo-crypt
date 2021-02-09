import java.io.*
import java.security.MessageDigest
import java.util.*
import kotlin.math.pow

class MerkleTree(private var root: String, private var height: Int) {
    private val encoder = Base64.getEncoder()
    private val decoder = Base64.getDecoder()
    private var nodes: IntArray = IntArray(height)
    internal val digest: MessageDigest = MessageDigest.getInstance("SHA-256")

    fun verify(dataBlock: String?, parentsHash: Array<String?>): Boolean {
        var dataHash = if (dataBlock == "null") null else
            String(Base64.getEncoder().encode(digest.digest(concat(byteArrayOf(0), Base64.getDecoder().decode(dataBlock)))))

        (0 until height).forEach { i ->
            val parentHash = if (parentsHash[i] == "null") null else parentsHash[i]
            dataHash = if (nodes[i] % 2 == 0) sha256(dataHash, parentHash) else sha256(parentHash, dataHash)
        }
        return dataHash == root
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

    internal fun enc(byteArray: ByteArray): ByteArray {
        return encoder.encode(byteArray)
    }

    internal fun dec(string: String?): ByteArray {
        return decoder.decode(string)
    }
}

fun Boolean.toInt() = if (this) 1 else 0

fun main(args: Array<String>) {
    val height = 8
    val itIsCode = BooleanArray(256)
    val itIsSecondCode = BooleanArray(256)
    val codes = Array(256) { Array(2) { Array(256) { ByteArray(32) } } }
    try {
        if (args.size == 1 && args[0] == "check") {
            val process = ProcessBuilder("java", "-jar", "Interact-task7.jar").start()
            BufferedReader(InputStreamReader(process.inputStream)).use { bufferedReader ->
                val printWriter = PrintWriter(process.outputStream)
                val merkleTree = MerkleTree(bufferedReader.readLine(), height)
                for (round in 0..999) {
                    println(round)
                    var isCorrectSignature = true
                    val codedSignature = ByteArray(256 * 32)
                    val id = bufferedReader.readLine().toInt()
                    (0..255).forEach { _ ->
                        printWriter.print(itIsCode[id].toInt())
                    }
                    printWriter.println()
                    printWriter.flush()

                    val signature = bufferedReader.readLine()
                    val basePublicKey = bufferedReader.readLine()

                    val decodedSignature = merkleTree.dec(signature)
                    val decodedPublicKey = merkleTree.dec(basePublicKey)

                    (0..255).forEach { i ->
                        val help = ByteArray(32)
                        System.arraycopy(decodedSignature, 32 * i, help, 0, 32)
                        System.arraycopy(merkleTree.digest.digest(help), 0, codedSignature, 32 * i, 32)
                    }

                    codedSignature.indices.forEach { i ->
                        if (codedSignature[i] != decodedPublicKey[if (itIsCode[id]) 256 * 32 + i else i]) {
                            isCorrectSignature = false
                        }
                    }

                    val parentsHash = arrayOfNulls<String>(height)
                    for (i in 0 until height) {
                        parentsHash[i] = bufferedReader.readLine()
                    }

                    merkleTree.setup(id, height, 0, 0)
                    val isCorrectProof = merkleTree.verify(basePublicKey, parentsHash)
                    val hash = bufferedReader.readLine()

                    if (isCorrectSignature && isCorrectProof) {
                        (0..255).forEach { i ->
                            System.arraycopy(decodedSignature, 32 * i, codes[id][itIsCode[id].toInt()][i], 0, 32)
                        }
                        itIsSecondCode[id] = itIsCode[id]
                        itIsCode[id] = true
                        printWriter.println("YES")
                    } else {
                        printWriter.println("NO")
                    }
                    printWriter.flush()

                    if (itIsSecondCode[id]) {
                        val result = ByteArray(256 * 32)
                        (0..255).forEach { i ->
                            System.arraycopy(codes[id][('1' == hash[i]).toInt()][i], 0, result, 32 * i, 32)
                        }
                        val key = String(merkleTree.enc(result))
                        printWriter.println("YES\n$key")
                        println(key)
                        printWriter.flush()
                        break
                    } else {
                        printWriter.println("NO")
                        printWriter.flush()
                    }
                }
            }
        } else {
            BufferedReader(InputStreamReader(System.`in`)).use { bufferedReader ->
                val merkleTree = MerkleTree(bufferedReader.readLine(), height)
                for (round in 0..999) {
                    var isCorrectSignature = true
                    val codedSignature = ByteArray(256 * 32)
                    val id = bufferedReader.readLine().toInt()
                    (0..255).forEach { _ ->
                        print(itIsCode[id].toInt())
                    }
                    println()

                    val signature = bufferedReader.readLine()
                    val basePublicKey = bufferedReader.readLine()

                    val decodedSignature = merkleTree.dec(signature)
                    val decodedPublicKey = merkleTree.dec(basePublicKey)

                    (0..255).forEach { i ->
                        val help = ByteArray(32)
                        System.arraycopy(decodedSignature, 32 * i, help, 0, 32)
                        System.arraycopy(merkleTree.digest.digest(help), 0, codedSignature, 32 * i, 32)
                    }

                    codedSignature.indices.forEach { i ->
                        if (codedSignature[i] != decodedPublicKey[if (itIsCode[id]) 256 * 32 + i else i]) {
                            isCorrectSignature = false
                        }
                    }

                    val parentsHash = arrayOfNulls<String>(height)
                    for (i in 0 until height) {
                        parentsHash[i] = bufferedReader.readLine()
                    }

                    merkleTree.setup(id, height, 0, 0)
                    val isCorrectProof = merkleTree.verify(basePublicKey, parentsHash)
                    val hash = bufferedReader.readLine()

                    if (isCorrectSignature && isCorrectProof) {
                        (0..255).forEach { i ->
                            System.arraycopy(decodedSignature, 32 * i, codes[id][itIsCode[id].toInt()][i], 0, 32)
                        }
                        itIsSecondCode[id] = itIsCode[id]
                        itIsCode[id] = true
                        println("YES")
                    } else {
                        println("NO")
                    }

                    if (itIsSecondCode[id]) {
                        val result = ByteArray(256 * 32)
                        (0..255).forEach { i ->
                            System.arraycopy(codes[id][('1' == hash[i]).toInt()][i], 0, result, 32 * i, 32)
                        }
                        println("YES")
                        println(String(merkleTree.enc(result)))
                        break
                    } else {
                        println("NO")
                    }
                }
            }
        }
    } catch (e: IOException) {
        System.err.println("ERROR: Input - " + e.localizedMessage)
    }
}