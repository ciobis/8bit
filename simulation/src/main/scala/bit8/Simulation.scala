package bit8

import bit8.simulation.Computer

object Simulation {

  def main(args: Array[String]): Unit = {
    val code: List[String] = List(
      "DB x",
      "DB y",
      "DB direction",
      "MOV x,5",
      "MOV y,0",
      "MOV direction,0",

      "loop:",
      "MOV direction,INPUT",

      "CMP direction,1",
      "JNE notUp",
      "CALL clearCurrent",
      "SUB y,1",
      "notUp:",

      "CMP direction,2",
      "JNE notRight",
      "CALL clearCurrent",
      "ADD x,1",
      "notRight:",

      "CMP direction,3",
      "JNE notDown",
      "CALL clearCurrent",
      "ADD y,1",
      "notDown:",

      "CMP direction,4",
      "JNE notLeft",
      "CALL clearCurrent",
      "SUB x,1",
      "notLeft:",

      "MOV OUT,x",
      "MOV OUT,y",
      "MOV OUT,72",

      "JMP loop",
      "HLT",

      "clearCurrent:",
      "MOV OUT,x",
      "MOV OUT,y",
      "MOV OUT,32",
      "RET",
    )
    Computer.runWithIO(code)
  }

}
