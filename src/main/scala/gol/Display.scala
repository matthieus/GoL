package gol

import java.awt.Dimension
import scala.collection.mutable.Buffer
import scala.swing.event.ButtonClicked
import scala.swing._

import javax.swing.border.BevelBorder

object GoLDisplay extends SimpleSwingApplication {
  val width, height = 20
  val grid = new BoardGrid(width, height, Set((5, 5), (5, 6), (5, 7)))

  def top = new BoardFrame(grid)
}

class BoardFrame(grid: BoardGrid) extends MainFrame {
  title = "Game of Life"
  minimumSize = new Dimension(500, 500)
  contents = new BoxPanel(Orientation.Vertical) {
    contents += grid
    val startButton = StartButton()
    contents += startButton
    listenTo(startButton)
    reactions += {
      case ButtonClicked(b: StartButton) => {
        new BoardLife(grid).start()
      }
    }
  }
}

class BoardLife(grid: BoardGrid) extends Thread {
  override def run = {
    var board = Board(grid.aliveStart)
    for (i <- 0 to 10) {
      board = board.next
      Swing.onEDT {
        grid.setAliveCells(board.alive)
      }
      Thread.sleep(500)
    }
  }
}

class BoardGrid(
    width: Int,
    height: Int,
    var aliveStart: Set[(Int, Int)] = Set()) extends GridPanel(width, height) {
  
  setAliveCells(aliveStart)
  
  def setAliveCells(aliveCells: Set[(Int, Int)]) = {
    contents.clear()
    addCellButtons(aliveCells)
    revalidate()
    repaint()
  }  

  def addCellButtons(aliveCells: Set[(Int, Int)]) = {
    cellButtonsInitialized(aliveCells)
      .foreach(_
        .foreach { b: CellButton =>
          contents += b
          listenTo(b)
        })
  }
  
  def cellButtonsInitialized(aliveCells: Set[(Int, Int)] = Set()): Seq[Seq[CellButton]] =
    for (j <- height - 1 to 0 by -1)
      yield (for (i <- 0 to width - 1)
        yield (CellButton((i, j), aliveCells.contains((i, j)))))
        
  reactions += {
    case ButtonClicked(b: CellButton) => {
      b.setAlive(!b.alive)
      if (b.alive) {
        aliveStart += b.cell
      } else {
        aliveStart -= b.cell
      }
    }
  }
}

case class CellButton(cell: (Int, Int), var alive: Boolean = false) extends Button("") {
  // name is for testing purpose
  name = "("+cell._1+","+cell._2+")"
  opaque = true
  borderPainted = true
  border = new BevelBorder(BevelBorder.RAISED)
  updateAliveDisplay

  def setAlive(isAlive: Boolean): Unit = {
    alive = isAlive
    updateAliveDisplay
  }

  private def updateAliveDisplay: Unit = {
    if (alive) {
      background = java.awt.Color.BLACK
    } else
      background = java.awt.Color.WHITE
  }

}
case class StartButton() extends Button("start")