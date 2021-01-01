package bit8.simulation

import java.awt.event.ActionEvent

import javax.swing.{AbstractAction, JComponent, JFrame, JPanel, JTextArea, KeyStroke, WindowConstants}
import java.awt.{Color, Font}

class OutputModel(val width: Int,
                  val height: Int,
                  val onDisplay: (String) => Unit) {
  private sealed trait State
  private object WaitX extends State
  private object WaitY extends State
  private object WaitValue extends State

  private var state: State = WaitX
  private var x = 0
  private var y = 0
  private var outputText: String = " " * (width * height)

  def setData(data: Int): Unit = {
    state match {
      case WaitX => {
        x = data
        state = WaitY
      }
      case WaitY => {
        y = data
        state = WaitValue
      }
      case WaitValue => {
        outputText = outputText.updated(y * width + x, data.toChar)
        onDisplay(outputText)
        state = WaitX
      }
    }
  }
}

class InputModel(comp: JComponent) {
  private var onData: Int => Unit = (_) => ()

  registerKeyBinding("UP", 1)
  registerKeyBinding("RIGHT", 2)
  registerKeyBinding("DOWN", 3)
  registerKeyBinding("LEFT", 4)

  private def registerKeyBinding(keyStroke: String, value: Int): Unit = {
    comp.getInputMap.put(KeyStroke.getKeyStroke(keyStroke), "custom_" + keyStroke)
    comp.getInputMap.put(KeyStroke.getKeyStroke("released " + keyStroke), "custom_released_" + keyStroke)
    comp.getActionMap.put("custom_" + keyStroke, new AbstractAction() {
      override def actionPerformed(e: ActionEvent): Unit = {
        onData(value)
        System.out.println(keyStroke)
      }
    })
    comp.getActionMap.put("custom_released_" + keyStroke, new AbstractAction() {
      override def actionPerformed(e: ActionEvent): Unit = {
        onData(0)
        System.out.println("released " + keyStroke)
      }
    })
  }

  def onNewData(onData: Int => Unit): Unit = {
    this.onData = onData
  }

}


class WindowDisplay(width: Int, height: Int) extends JFrame {
  val textArea = new JTextArea(height, width)
  textArea.setFont(new Font("monospaced", Font.BOLD, 14))
  textArea.setBackground(Color.BLACK)
  textArea.setForeground(Color.WHITE)
  textArea.setLineWrap(true)
  textArea.setEditable(false)

  add(textArea)
  setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  setVisible(true)
  pack()
}

class IO(val width: Int = 50, val height: Int = 50) {
  private val display = new WindowDisplay(width, height)

  val outputModel = new OutputModel(width, height, display.textArea.setText)
  val inputModel = new InputModel(display.textArea)
}
