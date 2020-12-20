package bit8.simulation.components.wire

sealed trait State
case object High extends State
case object Low extends State
case object Ground extends State

object State {
  def fromBoolean(b: Boolean): State = if(b) High else Low
}

trait StateChangeListener {
  def onStateChange(lastState: State, newState: State): Unit
}


object StateChangeListener {
  def apply(listener: PartialFunction[(State, State), Unit]): StateChangeListener =  new StateChangeListener {
    override def onStateChange(lastState: State, newState: State): Unit = {
      if (listener.isDefinedAt((lastState, newState))) {
        listener.apply((lastState, newState))
      }
    }
  }

  def fromNewState(listener: PartialFunction[State, Unit]): StateChangeListener =  new StateChangeListener {
    override def onStateChange(lastState: State, newState: State): Unit = {
      if (listener.isDefinedAt(newState)) {
        listener.apply(newState)
      }
    }
  }
}

private [wire] case class Wire(left: Connection, right: Connection) {
  def connections(): (Connection, Connection) = (left, right)

  def isLow = left.wire.isLow

  def isHigh = left.wire.isHigh
}

private [wire] case class Wiring(conns: Seq[Connection]) {
  private var stateChangeListeners: List[StateChangeListener] = List.empty

  private var state: State = calculateState()

  def getState(): State = state

  def +(other: Wiring): Wiring = {
    val networkConns = this.conns ++ other.conns
    val networkListeners = this.stateChangeListeners ++ other.stateChangeListeners

    val newNetwork = Wiring(networkConns)
    newNetwork.stateChangeListeners = networkListeners

    newNetwork
  }

  def connectionUpdated(connection: Connection): Unit = {
    val newState = calculateState()
    if (newState != state) {
      val lastState = state
      state = newState

      stateChangeListeners.foreach(_.onStateChange(lastState, newState))
    }
  }

  def onNewState(listener: PartialFunction[State, Unit]): Unit = addStateChangeListener(StateChangeListener.fromNewState(listener))

  private def addStateChangeListener(listener: StateChangeListener): Unit = stateChangeListeners = stateChangeListeners :+ listener

  private def calculateState(): State =
    if (conns.exists(_.getState() == High)) High
    else Low

  def isLow = state == Ground || state == Low

  def isHigh = state == High
}







case class Connection () {
  private var state: State = Low

  var wire: Wiring = Wiring(Seq(this))

  def updateState(newSate: State): Unit = {
    if (state != newSate) {
      state = newSate
      wire.connectionUpdated(this)
    }
  }

  def getState(): State = state

  def join(other: Connection): Unit = {
    val sharedNetwork = this.wire + other.wire
    this.wire.conns.foreach(_.wire = sharedNetwork)
    other.wire.conns.foreach(_.wire = sharedNetwork)
  }

  override def toString: String = s"Connection(state=${state}, wiringState=${wire.getState()})"
}

object Connection {
  def HIGH: Connection = withState(High)
  def GROUND: Connection = withState(Ground)
  def LOW: Connection = withState(Low)

  def wire(): Wire = {
    val left: Connection = Connection()
    val right: Connection = Connection()
    left.join(right)

    Wire(left, right)
  }

  private def withState(state: State): Connection = {
    val w = Connection.wire()
    w.left.updateState(state)
    w.right
  }
}


