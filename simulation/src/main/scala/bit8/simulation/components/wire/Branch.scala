package bit8.simulation.components.wire

object Branch {

  def branch(c: Connection): (Connection, Connection) = {
    val w1 = Connection.wire()
    val w2 = Connection.wire()

    c.join(w1.left)
    c.join(w2.left)

    val external1 = w1.right
    val external2 = w2.right

    (external1, external2)
  }

  def branch3(c: Connection): (Connection, Connection, Connection) = {
    val w1 = Connection.wire()
    val w2 = Connection.wire()
    val w3 = Connection.wire()

    c.join(w1.left)
    c.join(w2.left)
    c.join(w3.left)

    val external1 = w1.right
    val external2 = w2.right
    val external3 = w3.right

    (external1, external2, external3)
  }

  def branch4(c: Connection): (Connection, Connection, Connection, Connection) = {
    val w1 = Connection.wire()
    val w2 = Connection.wire()
    val w3 = Connection.wire()
    val w4 = Connection.wire()

    c.join(w1.left)
    c.join(w2.left)
    c.join(w3.left)
    c.join(w4.left)

    val external1 = w1.right
    val external2 = w2.right
    val external3 = w3.right
    val external4 = w4.right

    (external1, external2, external3, external4)
  }

  def branch: (Connection, Connection, Connection) = {
    val w = Connection.wire()
    val (b1, b2) = branch(w.right)
    (w.left, b1, b2)
  }


}
