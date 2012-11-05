package util
import org.specs2.mutable.FragmentsBuilder
import scala.swing.Swing

trait StepOnEDT { this: FragmentsBuilder =>  
  def apply(f: => Any) = new Thread { override def run = f }.start()
}