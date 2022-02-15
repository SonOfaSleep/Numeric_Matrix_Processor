import java.math.RoundingMode
import kotlin.math.pow
import kotlin.math.roundToInt

fun main() {
    while (true) {
        println("""
            1. Add matrices
            2. Multiply matrix by a constant
            3. Multiply matrices
            4. Transpose matrix
            5. Calculate a determinant
            6. Inverse matrix
            0. Exit
        """.trimIndent())
        println("Your choice:")
        when(readLine()!!) {
            "1" -> addMatrices()
            "2" -> constantMultiply()
            "3" -> matricesMultiply()
            "4" -> chooseTranspose()
            "5" -> calculateDeterminant()
            "6" -> inverseMatrix()
            "0" -> return
        }
        println()
    }
}

fun calculateDeterminant() {
    val matrix = singleMatrixInput()

    if (!isSquare(matrix)) {
        println("The operation cannot be performed.")
    } else {
        println("The result is:")
        println(recursiveDeterminant(matrix))
    }
}

fun inverseMatrix() {
    val matrix = singleMatrixInput()
    val isSquare = isSquare(matrix)
    val detMatrix = if (isSquare) recursiveDeterminant(matrix) else 0.0

    if (!isSquare || detMatrix == 0.0) {
        println("This matrix doesn't have an inverse.")
    } else {
        val adjMatrix = findAdjoint(matrix)
        val inverseMatrix = multiplyByConst(adjMatrix, (1.0 / detMatrix))
        println("The result is:")
        printMatrix(inverseMatrix)
    }
}

fun findAdjoint(matrix: List<MutableList<Double>>): List<MutableList<Double>> {
    val newMatrix = MutableList(matrix.size){ mutableListOf<Double>() }

    for (rowIndex in matrix.indices) {
        for (columnIndex in matrix[rowIndex].indices) {
            newMatrix[rowIndex].add(cofactor(matrix, rowIndex, columnIndex))
        }
    }
    return mainTranspose(newMatrix)
}

fun cofactor(matrix: List<MutableList<Double>>, row: Int, column: Int): Double {
    return (-1.0).pow((row + 1) + (column + 1)) * recursiveDeterminant(smallerMatrix(matrix, row, column))
}

fun isSquare(matrix: List<MutableList<Double>>): Boolean {
    var bool = true
    for (row in matrix) {
        if (row.size != matrix.size) bool = false
        break
    }
    return bool
}

fun recursiveDeterminant(matrix: List<MutableList<Double>>): Double {
    return if (matrix.size == 1) {
        matrix[0][0]
    } else if(matrix.size == 2) {
        matrix[0][0] * matrix[1][1] - matrix[1][0] * matrix[0][1]
    } else {
        // cofactor expansion
        var answer = 0.0
        for (j in 0..matrix.lastIndex) {
            val cofactor = (-1.0).pow(j) * matrix[0][j] * recursiveDeterminant(smallerMatrix(matrix, 0, j))
            answer += cofactor
        }
        return answer
    }
}

fun smallerMatrix(matrix: List<MutableList<Double>>, row: Int, column: Int): List<MutableList<Double>> {
    val newMatrix = MutableList(matrix.size){ mutableListOf<Double>() }
    for (rowIndex in matrix.indices) {
        for (elementIndex in matrix[rowIndex].indices) {
            newMatrix[rowIndex].add(matrix[rowIndex][elementIndex])
        }
    }
    newMatrix.removeAt(row)
    for (line in newMatrix) {
        line.removeAt(column)
    }
    return newMatrix
}

fun addMatrices() {
    val matrix1 = inputMatrix("first")
    val matrix2 = inputMatrix("second")

    if (!matrixSizeComparison(matrix1, matrix2)) {
        println("The operation cannot be performed.")
    } else {
        println("The result is:")
        printMatrix(sumMatrix(matrix1, matrix2))
    }
}

fun sumMatrix(matrix1: List<MutableList<Double>>, matrix2: List<MutableList<Double>>): List<MutableList<Double>> {
    for (row in matrix1.indices) {
        for (column in matrix1[row].indices) {
            matrix1[row][column] = matrix1[row][column] + matrix2[row][column]
        }
    }
    return matrix1
}

fun inputMatrix(name: String): List<MutableList<Double>> {
    println("Enter size of $name matrix:")
    val (r, c) = readLine()!!.split("\\s+".toRegex())
    println("Enter $name matrix:")
    return readMatrix(r.toInt())
}

fun readMatrix(rows: Int): List<MutableList<Double>> {
    val matrix = List(rows){ mutableListOf<Double>() }
    for (i in matrix.indices) {
        val input = readLine()!!.split(" ")
        for (num in input) {
            matrix[i].add(num.toDouble())
        }
    }
    return matrix
}

fun constantMultiply() {
    val matrix = singleMatrixInput()
    println("Enter constant:")
    val const = readLine()!!.toDouble()
    println("The result is:")
    printMatrix(multiplyByConst(matrix, const))
}

fun singleMatrixInput(): List<MutableList<Double>> {
    println("Enter matrix size:")
    val (r, c) = readLine()!!.split("\\s+".toRegex())
    println("Enter matrix:")
    return readMatrix(r.toInt())
}

fun multiplyByConst(matrix: List<MutableList<Double>>, const: Double): List<MutableList<Double>> {
    for (row in matrix.indices) {
        for (column in matrix[row].indices) {
            val mult = (matrix[row][column] * const).toBigDecimal().setScale(2, RoundingMode.DOWN).toDouble()
            matrix[row][column] = mult
        }
    }
    return matrix
}

fun matricesMultiply() {
    val matrix1 = inputMatrix("first")
    val matrix2 = inputMatrix("second")

    if (matrix1[0].size != matrix2.size) {
        println("The operation cannot be performed.")
    } else {
        println("The result is:")
        printMatrix(multMatrix(matrix1, matrix2))
    }
}
fun multMatrix(matrix1: List<MutableList<Double>>, matrix2: List<MutableList<Double>>): List<MutableList<Double>> {
    val newMatrix = List(matrix1.size){ mutableListOf<Double>() }

    for (rowIndex in matrix1.indices) {
        for (columnIndex in matrix2[0].indices) {
            var newNum = 0.0
            for (numIndex in matrix2.indices) {
                newNum += matrix1[rowIndex][numIndex] * matrix2[numIndex][columnIndex]
            }
            newMatrix[rowIndex].add(newNum)
        }
    }
    return newMatrix
}

fun chooseTranspose() {
    println("""
        1. Main diagonal
        2. Side diagonal
        3. Vertical line
        4. Horizontal line
    """.trimIndent())
    println("Your choice:")
    val choice = readLine()!!
    val matrix = singleMatrixInput()
    println("The result is:")
    when(choice) {
        "1" -> printMatrix(mainTranspose(matrix))
        "2" -> printMatrix(sideTranspose(matrix))
        "3" -> printMatrix(verticalTranspose(matrix))
        "4" -> printMatrix(horizontalTranspose(matrix))
    }
}

fun mainTranspose(matrix: List<MutableList<Double>>): List<MutableList<Double>> {
    val newMatrix = List(matrix[0].size){ mutableListOf<Double>() }
    for (listIndex in matrix.indices) {
        for (numIndex in matrix[listIndex].indices) {
            newMatrix[numIndex].add(matrix[listIndex][numIndex])
        }
    }
    return newMatrix
}

fun sideTranspose(matrix: List<MutableList<Double>>): List<MutableList<Double>> {
    return verticalTranspose(horizontalTranspose(mainTranspose(matrix)))
}

fun verticalTranspose(matrix: List<MutableList<Double>>): List<MutableList<Double>> {
    matrix.forEach { it.reverse() }
    return matrix
}

fun horizontalTranspose(matrix: List<MutableList<Double>>): List<MutableList<Double>>  {
    val newMatrix = matrix.toMutableList()
    return newMatrix.reversed()
}

fun matrixSizeComparison(matrix1: List<MutableList<Double>>, matrix2: List<MutableList<Double>>): Boolean {
    var count1 = 0
    for (i in matrix1) { for (j in i) { count1++ } }
    var count2 = 0
    for (i in matrix2) { for (j in i) { count2++ } }
    return count1 == count2
}

fun printMatrix(matrix: List<MutableList<Double>>) {
    // checking for fracture part. If fracture present, print as is. Else - transform to new matrix with Int's.
    if (checkFracture(matrix)) {
        matrix.forEach { println(it.joinToString(" ")) }
    } else {
        val new = List(matrix.size) { mutableListOf<Int>() }
        for (listIndex in matrix.indices) {
            for (numIndex in matrix[listIndex].indices) {
                new[listIndex].add(matrix[listIndex][numIndex].roundToInt())
            }
        }
        new.forEach { println(it.joinToString(" ")) }
    }
}

fun checkFracture(matrix: List<MutableList<Double>>): Boolean {
    var bool = false
    mother@ for (list in matrix) {
        for (num in list) {
            if (num % 1 > 0) {
                bool = true
                break@mother
            }
        }
    }
    return bool
}