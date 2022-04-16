package board

import board.Direction.*

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)

open class SquareBoardImpl(override val width: Int) : SquareBoard {
    val board: Array<Array<Cell>> = Array(width) {
        row -> Array(width) {
            col -> Cell(row + 1, col + 1)
    } }

    fun Array<Array<Cell>>.get(outerRowIndex: Int, outerColIndex: Int): Cell {
        return this[outerRowIndex - 1][outerColIndex - 1]
    }

    private fun Int.inRange() = this in 1 until width + 1

    override fun getCellOrNull(i: Int, j: Int): Cell? = board.getOrNull(i - 1)?.getOrNull(j - 1)

    override fun getCell(i: Int, j: Int): Cell
        = if (i.inRange() && j.inRange()) board.get(i, j)
            else throw IllegalArgumentException()

    override fun getAllCells(): Collection<Cell>
    = board.flatMap { it.asIterable() }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        val subRow = mutableListOf<Cell>()
        for (col in jRange) {
            if (col.inRange())
                subRow.add(board.get(i, col))
        }
        return subRow
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        val subCol = mutableListOf<Cell>()
        for (row in iRange) {
            if (row.inRange())
                subCol.add(board.get(row, j))
        }
        return subCol
    }

    fun getFirstCellIndex(cell: Cell) : Pair<Int, Int>? {
        board.withIndex().forEach { (rowIndex, row) -> row.withIndex().forEach { (colIndex, innerCell) ->
            if (innerCell == cell) return Pair(rowIndex + 1, colIndex + 1)
        }}
        return null // if not found
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        fun Pair<Int, Int>.neighbourIndexOrNull(rowIncrem: Int, colIncrem: Int) : Pair<Int, Int>? {
            val (outerRowInd, outerColInd) = this
            return if ((outerRowInd + rowIncrem).inRange() && (outerColInd + colIncrem).inRange())
                Pair(outerRowInd + rowIncrem, outerColInd + colIncrem)
            else null
        }

        val index = getFirstCellIndex(this) ?: return null
        // neighbour out index
        val nIndex = when(direction) {
            UP -> index.neighbourIndexOrNull(-1, 0)
            DOWN -> index.neighbourIndexOrNull(1, 0)
            RIGHT -> index.neighbourIndexOrNull(0, 1)
            LEFT -> index.neighbourIndexOrNull(0, -1)
        }

        return if (nIndex == null) {
            null
        } else {
            board.get(nIndex.first, nIndex.second)
        }
    }

    // for debugging
    fun getNeighbourForCell(rowIndex: Int, colIndex: Int, direction: Direction): Cell? {
        return board.get(rowIndex, colIndex).getNeighbour(direction)
    }
}

class GameBoardImpl<T>(width: Int) : SquareBoardImpl(width), GameBoard<T> {
    private val cellVals = mutableMapOf<Cell, T?>(*super.getAllCells().map { Pair<Cell, T?>(it, null) }.toList().toTypedArray())

    operator fun set(rowIndex: Int, colIndex: Int, value: T?) {
        cellVals[board.get(rowIndex, colIndex)] = value
    }

    override fun get(cell: Cell): T? = cellVals[cell]

    override fun set(cell: Cell, value: T?) {
        cellVals[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell>
        = cellVals.filter { (_, value) -> predicate(value) }.keys

    override fun find(predicate: (T?) -> Boolean): Cell?
        = cellVals.toList().find { (_, value) -> predicate(value) }?.first

    override fun any(predicate: (T?) -> Boolean): Boolean
        = cellVals.values.any(predicate)

    override fun all(predicate: (T?) -> Boolean): Boolean
        = cellVals.values.all(predicate)

}

fun main() {
    val sBoard = SquareBoardImpl(3)
    println(sBoard.board.forEach { println(it.joinToString { cell -> cell.toString() }) })
    println(sBoard.getAllCells())
    println(sBoard.getRow(1, 1..2))
    println(sBoard.getRow(1, 3 downTo 1))
    println(sBoard.getColumn(1..2, 2))
    println(sBoard.getColumn(3 downTo 1, 2))
    println(sBoard.getColumn(4 downTo -1, 2))
//    println(sBoard.board.get(1, 1))
    println(sBoard.getNeighbourForCell(1,1, DOWN))
    println(sBoard.getNeighbourForCell(1,1, UP))
}