package util
import org.specs2.specification.Scope
import scala.swing.Swing
import org.specs2.mutable.After
import org.specs2.execute.Result

trait ContextOnEDT extends Scope{
  def apply[T <% Result](f: => T) = { Swing.onEDT(f) }
}
