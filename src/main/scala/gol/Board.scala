package gol

/**
 * The Board case class contains the whole logic for the Game of Life
 */
// BEGIN - the game of life itself
case class Board(alive: Set[(Int, Int)]) {

  
  def next = Board(
    alive.filter(aliveWillLive(_)) ++
    deadThatCouldLive.filter(deadWillLive(_)))

  def deadWillLive(deadCell: (Int, Int)): Boolean = {
    val nbAliveAround = 8 - deadNeighbours(deadCell).size
    nbAliveAround == 3
  }

  def aliveWillLive(aliveCell: (Int, Int)): Boolean = {
    val nbAliveAround = 8 - deadNeighbours(aliveCell).size
    nbAliveAround >= 2 && nbAliveAround <= 3
  }

  def deadNeighbours(cell: (Int, Int)) = {
    for (
      i <- -1 to 1 map (_ + cell._1);
      j <- -1 to 1 map (_ + cell._2);
      if ((i, j) != cell &&
        !alive.contains((i, j)))
    ) yield (i, j)
  }

  def deadThatCouldLive = alive.flatMap(deadNeighbours(_)) toSet // toSet will remove any duplicate

// END
  
  override def toString = {
    val (minX, minY, maxX, maxY) = minXminYmaxXmaxY
    val sb = new StringBuilder()
    (for (
      j <- maxY + 2 to minY - 2 by -1; // highest y coordinate first
      i <- minX - 2 to maxX + 2
    ) yield (
      if (alive.contains((i, j))) " X " else "   ") +
      (if (i == maxX + 2) "\n" else "")) // new line if end of row
      .foldLeft(sb)(_ append _)
    sb.toString
  }

  def minXminYmaxXmaxY: (Int, Int, Int, Int) = {
    def recursiveMinXminYmaxXmaxY(cells: List[(Int, Int)]): (Int, Int, Int, Int) = {
      if (cells isEmpty)
        return (0,0,0,0)
      val (x,y) :: xs = cells
      if (xs == Nil)
        (x, y, x, y)
      else {
        var (minX, minY, maxX, maxY) = recursiveMinXminYmaxXmaxY(xs)
        if (x < minX)
          minX = x
        if (y < minY)
          minY = y
        if (x > maxX)
          maxX = x
        if (y > maxY)
          maxY = y
        (minX, minY, maxX, maxY)
      }
    }
    recursiveMinXminYmaxXmaxY(alive toList)
  }
}