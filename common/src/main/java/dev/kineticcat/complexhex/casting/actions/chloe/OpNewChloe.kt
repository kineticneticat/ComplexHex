//package dev.kineticcat.complexhex.casting.actions.chloe
//
//import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
//import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
//import at.petrak.hexcasting.api.casting.iota.Iota
//import dev.kineticcat.complexhex.api.casting.iota.ChloeIota
//
//object OpNewChloe : ConstMediaAction {
//    override val argc = 0
//    val chars = listOf("0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F")
//    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
//        var name = ""
//        name += chars.random()
//        name += chars.random()
//        name += chars.random()
//        name += chars.random()
//        name += chars.random()
//        return listOf(ChloeIota(name, ChloeIota.State.MASTER))
//    }
//}