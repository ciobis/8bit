import bit8.simulation.Computer
import org.scalatest.BeforeAndAfter
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

class ComputerSpec extends AnyFeatureSpec with GivenWhenThen with BeforeAndAfter {

  Feature("Runs all the programs") {

    Scenario("Variables") {
      assert(Computer.run(
        "DB var1",
        "DB var2",

        "MOV var1,65",
        "MOV var2,65",
        "MOV A,var1",
        "MOV B,var2",
        "ADD A,B",
        "MOV var1,A",

        "MOV A,10",
        "MOV B,10",
        "ADD A,B",
        "MOV B,var1",
        "ADD A,B",

        "MOV OUT,A",
        "HLT",
      ) == 150)

      assert(Computer.run(
        "DB result",

        "MOV result,65",
        "MOV B,65",
        "ADD result,B",
        "MOV A,65",
        "ADD result,A",
        "MOV OUT,result",

        "HLT",
      ) == 195)
    }

    Scenario("Registers") {
      assert(Computer.run(
        "MOV A,65",
        "MOV B,A",
        "ADD A,B",
        "MOV OUT,A",

        "HLT",
      ) == 130)

      assert(Computer.run(
        "MOV A,65",
        "MOV B,A",
        "MOV A,5",
        "ADD A,B",
        "MOV OUT,A",

        "HLT",
      ) == 70)

      assert(Computer.run(
        "MOV A,128",
        "MOV B,9",
        "SUB A,B",
        "MOV OUT,A",

        "HLT",
      ) == 119)
    }

    Scenario("Jump") {
      assert(Computer.run(
        "DB result",
        "MOV result,100",
        "JMP skip2B",
        "MOV B,2",
        "ADD result,B",
        "ADD result,B",
        "skip2B:",
        "MOV B,1",
        "ADD result,B",
        "MOV OUT,result",

        "HLT",
      ) == 101)

      //jump over more than 1 byte
      val mov = (0 to 301).map(_ => "MOV A,100")
      assert(Computer.run(List(
        "DB result",
        "MOV result,100",
        "JMP skip2B",
      ) ++ mov ++ List(
        "MOV B,2",
        "ADD result,B",
        "skip2B:",
        "MOV B,1",
        "ADD result,B",
        "MOV OUT,result",

        "HLT",
      )) == 101)
    }

    Scenario("Ram") {
      assert(Computer.run(
        "MOV ML,0",
        "MOV MH,0",
        "MOV [MX],2",

        "MOV ML,1",
        "MOV MH,1",
        "MOV [MX],11",

        "MOV ML,0",
        "MOV MH,0",
        "MOV A,[MX]",

        "MOV ML,1",
        "MOV MH,1",
        "MOV B,[MX]",

        "ADD A,B",
        "MOV OUT,A",

        "HLT",
      ) == 13)

      assert(Computer.run(
        "ADD ML,1",
        "ADD MH,1",
        "MOV [MX],123",

        "ADD ML,1",
        "ADD MH,1",
        "MOV [MX],77",

        "MOV ML,1",
        "MOV MH,1",
        "ADD B,[MX]",

        "MOV ML,2",
        "MOV MH,2",
        "ADD B,[MX]",

        "MOV OUT,B",

        "HLT",
      ) == 200)
    }

    Scenario("Procedures") {
      assert(Computer.run(
        "DB result",

        "CALL add13",
        "MOV B,4",
        "ADD result,B",
        "MOV OUT,result",
        "HLT",

        "add13:",
        "CALL add7",
        "MOV B,6",
        "ADD result,B",
        "RET",

        "add7:",
        "CALL add5",
        "MOV B,2",
        "ADD result,B",
        "RET",

        "add5:",
        "CALL add3",
        "MOV B,2",
        "ADD result,B",
        "RET",

        "add3:",
        "MOV B,3",
        "ADD result,B",
        "RET",
      ) == 17)
    }

    Scenario("CMP JE JNE") {
      assert(Computer.run(
        "DB nFib",
        "DB counter",
        "DB fib1",
        "DB fib2",
        "DB tmp",

        "MOV nFib,14",
        "MOV counter,2",
        "MOV fib1,0",
        "MOV fib2,1",

        "loop:",
        "MOV tmp,fib1",
        "MOV fib1,fib2",
        "ADD fib2,tmp",

        "ADD counter,1",
        "CMP counter,nFib",
        "JNE loop",

        "MOV OUT,fib2",
        "HLT"
      ) == 233)

      val equalsIfElseReturnCmpAB = (cmp1: Int, cmp2: Int, returnEq: Int, returnNeq: Int) => Seq(
        s"MOV A,$cmp1",
        s"MOV B,$cmp2",
        "CMP A,B",
        "JE equals",
        s"MOV A,$returnNeq",
        "MOV OUT,A",
        "JMP endIf",
        "equals:",
        s"MOV A,$returnEq",
        "MOV OUT,A",
        "endIf:",
        "HLT",
      )

      val equalsIfElseReturnCmpConstToVar = (cmp1: Int, cmp2: Int, returnEq: Int, returnNeq: Int) => Seq(
        "DB var",
        s"MOV var,$cmp1",

        s"CMP var,$cmp2",
        "JE equals",
        s"MOV A,$returnNeq",
        "MOV OUT,A",
        "JMP endIf",
        "equals:",
        s"MOV A,$returnEq",
        "MOV OUT,A",
        "endIf:",
        "HLT",
      )

      assert(Computer.run(equalsIfElseReturnCmpAB(200, 200, 123, 200): _*) == 123)
      assert(Computer.run(equalsIfElseReturnCmpAB(1, 2, 123, 200): _*) == 200)

      assert(Computer.run(equalsIfElseReturnCmpConstToVar(200, 200, 123, 200): _*) == 123)
      assert(Computer.run(equalsIfElseReturnCmpConstToVar(1, 2, 123, 200): _*) == 200)
    }

    Scenario("Input") {
      assert(Computer.runWithInput(
        "DB var",
        "MOV var,INPUT",
        "MOV A,INPUT",
        "ADD var,A",
        "MOV OUT,var",
        "HLT"
      )(87) == 174)
    }

    Scenario("SUB") {
      assert(Computer.run(
        "DB result",
        "DB x",

        "MOV x,10",
        "MOV result,65",

        "SUB result,1",
        "SUB result,x",
        "MOV OUT,result",

        "HLT",
      ) == 54)
    }

    Scenario("MOV var to MH/ML") {
      assert(Computer.run(
        "DB higher",
        "DB lower",

        "MOV MH,123",
        "MOV ML,124",
        "MOV [MX],179",
        "MOV MH,0",
        "MOV ML,0",

        "MOV higher,123",
        "MOV lower,124",
        "MOV MH,higher",
        "MOV ML,lower",
        "MOV A,[MX]",
        "MOV OUT,A",

        "HLT",
      ) == 179)
    }

    Scenario("ADD var to MH/ML") {
      assert(Computer.run(
        "DB higher",
        "DB lower",

        "MOV MH,123",
        "MOV ML,124",
        "MOV [MX],179",
        "MOV MH,0",
        "MOV ML,0",

        "MOV higher,23",
        "MOV lower,24",
        "MOV MH,100",
        "MOV ML,100",
        "ADD MH,higher",
        "ADD ML,lower",
        "MOV A,[MX]",
        "MOV OUT,A",

        "HLT",
      ) == 179)
    }

    Scenario("MOV var to memory") {
      assert(Computer.run(
        "DB var",
        "MOV var,179",
        "MOV MH,123",
        "MOV ML,124",
        "MOV [MX],var",
        "MOV A,0",
        "MOV A,[MX]",
        "MOV OUT,A",

        "HLT",
      ) == 179)
    }


  }

}
