package gol

import org.specs2.mutable
import org.specs2.specification.Scope
import org.fest.swing.edt.GuiActionRunner
import org.fest.swing.edt.GuiQuery
import org.fest.swing.fixture.FrameFixture
import org.fest.swing.core.GenericTypeMatcher
import scala.swing.Button
import org.fest.swing.core.BasicComponentFinder
import org.fest.swing.core.ComponentMatcher
import java.awt.Component
import javax.swing.JButton
import org.fest.swing.edt.FailOnThreadViolationRepaintManager
import scala.swing.Swing
import org.specs2.mutable.After
import util.StepOnEDT

object DisplayTest extends mutable.Specification {

  "click on a cell button should" ^ {
    "transform a cell to living cell if it is dead" ! new cell5_5clickedGrid {
      find(5,5).getBackground() must_== java.awt.Color.BLACK
    }
  }

  trait cell5_5clickedGrid extends After {
    val frame = GuiActionRunner.execute(new GuiQuery[BoardFrame]() {
      override def executeInEDT(): BoardFrame = { new BoardFrame(new BoardGrid(10, 10)) }})
    var window: FrameFixture = new FrameFixture(frame.peer)
    window.show()
    click(5, 5)
    def after = {
      window.cleanUp
    }
  }
  
  def click(x: Int, y: Int): Unit = {
      find(x,y)
      .doClick()    
  }
  
  def find(x: Int, y: Int): JButton = {
    BasicComponentFinder.finderWithCurrentAwtHierarchy()
    .find(new GenericTypeMatcher[JButton](classOf[JButton]) {
      override def isMatching(b: JButton) = {
        b.getName == "("+x+","+y+")"
      }
    })
  }
}