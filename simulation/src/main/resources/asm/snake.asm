DB headX
DB headY
DB tailX
DB tailY
DB inputDirection
DB headDirection
DB tailDirection
DB gridYStart

MOV headX,5
MOV headY,0
MOV tailX,5
MOV tailY,0
MOV inputDirection,0
MOV headDirection,2
MOV gridYStart,5

CALL initSnake

mainLoop:
  CALL setHeadDirection
  CALL placeHeadOnGrid
  CALL moveHead
  CALL placeHeadOnGrid
  CALL displayHead
  CALL clearTail
  CALL removeTailFromGrid
JMP mainLoop
HLT

placeHeadOnGrid:
  MOV MH,gridYStart
  ADD MH,headY
  MOV ML,headX
  MOV [MX],headDirection
  RET

setHeadDirection:
  MOV MH,gridYStart
  ADD MH,headY
  MOV ML,headX
  MOV headDirection,[MX]
  MOV inputDirection,INPUT

  CMP inputDirection,0
  JE noInput
    MOV headDirection,inputDirection
  noInput:

  RET

moveHead:
  CMP headDirection,1
  JNE notUp
    SUB headY,1
  notUp:

  CMP headDirection,2
  JNE notRight
    ADD headX,1
  notRight:

  CMP headDirection,3
  JNE notDown
    ADD headY,1
  notDown:

  CMP headDirection,4
  JNE notLeft
    SUB headX,1
  notLeft:

  RET

removeTailFromGrid:
  MOV MH,gridYStart
  ADD MH,tailY
  MOV ML,tailX
  MOV tailDirection,[MX]
  MOV [MX],0

  CMP tailDirection,1
  JNE tailNotUp
    SUB tailY,1
  tailNotUp:

  CMP tailDirection,2
  JNE tailNotRight
    ADD tailX,1
  tailNotRight:

  CMP tailDirection,3
  JNE tailNotDown
    ADD tailY,1
  tailNotDown:

  CMP tailDirection,4
  JNE tailNotLeft
    SUB tailX,1
  tailNotLeft:

  RET

displayHead:
  MOV OUT,headX
  MOV OUT,headY
  MOV OUT,72
  RET

clearTail:
  MOV OUT,tailX
  MOV OUT,tailY
  MOV OUT,32
  RET

initSnake:
  CALL placeHeadOnGrid
  CALL displayHead
  CALL moveHead
  CALL placeHeadOnGrid
  CALL displayHead
  CALL moveHead
  CALL placeHeadOnGrid
  CALL displayHead
  CALL moveHead
  CALL placeHeadOnGrid
  CALL displayHead
  CALL moveHead
  CALL placeHeadOnGrid
  CALL displayHead
  CALL moveHead
  CALL placeHeadOnGrid
  CALL displayHead

  RET
