package single_cycle

import Chisel._

class ALU extends Module {
    val in = IO(Input(new Bundle {
        val op = UInt(3.W)
        val arg_1 = SInt(32.W)
        val arg_2 = SInt(32.W)
    }))

    val out = IO(Output(new Bundle {
        val result = SInt(32.W)
        val zero = Bool()
        val neg = Bool()
    }))

    private val op = in.op
    private val lhs = in.arg_1
    private val rhs = in.arg_2

    private val res = Wire(SInt(32.W))
    when (op === ALUOps.AND) {
        res := lhs & rhs
    } .elsewhen (op === ALUOps.OR) {
        res := lhs | rhs
    } .elsewhen (op === ALUOps.XOR) {
        res := lhs ^ rhs
    } .elsewhen (op === ALUOps.SLL) {
        res := lhs << rhs(4, 0)
    } .elsewhen (op === ALUOps.SRL) {
        res := lhs >> rhs(4, 0)
    } .elsewhen (op === ALUOps.SRA) {
        res := (lhs.asUInt >> rhs(4, 0)).asSInt
    } .otherwise {
        // ADD or SUB
        val real_rhs = Mux(op === ALUOps.SUB, -rhs, rhs)
        res := lhs + real_rhs
    }

    out.result  := res
    out.zero    := res === 0.S
    out.neg     := res(31).asBool
}
