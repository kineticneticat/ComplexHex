//package dev.kineticcat.complexhex.casting.actions.chloe
//
//import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
//import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
//import at.petrak.hexcasting.api.casting.iota.Iota
//import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
//import dev.kineticcat.complexhex.api.casting.iota.ChloeIota
//
//object OpCopyChloe : ConstMediaAction {
//    override val argc = 1
//    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
//        if (args[0] !is ChloeIota) throw MishapInvalidIota.of(args[0], 0, "ichlota")
//        val ichlota = args[0] as ChloeIota
//        return listOf(ChloeIota(ichlota.id(), ChloeIota.State.INTERMEDIARY))
//    }
//}