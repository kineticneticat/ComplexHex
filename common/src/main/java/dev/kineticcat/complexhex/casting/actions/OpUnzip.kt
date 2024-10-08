package dev.kineticcat.complexhex.casting.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota

object OpUnzip : ConstMediaAction {
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val I = args.getList(0, argc)
        I.forEach { iota -> if (!(iota is ListIota && iota.list.size() == 2)) throw MishapInvalidIota.of(ListIota(I), 0, "unzip_bad_element") }
        val L = I.map { iota -> (iota as ListIota).list }
        val P = L.map { list -> Pair(list.getAt(0), list.getAt(1)) }
        val O = P.unzip()
        return listOf(ListIota(O.first), ListIota(O.second))
    }
}