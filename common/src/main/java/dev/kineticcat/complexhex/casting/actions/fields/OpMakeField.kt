package dev.kineticcat.complexhex.casting.actions.fields

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import dev.kineticcat.complexhex.api.casting.iota.FieldIota
import dev.kineticcat.complexhex.api.util.Field
import dev.kineticcat.complexhex.util.DataStorage

object OpMakeField : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val ds = DataStorage.getServerData(env.world.server)
        var name = FieldIota.makeName()
        while (ds.Fields.containsKey(name)) {
            name = FieldIota.makeName()
        }
        var shape = args.getList(0, argc).map {
            iota -> if (iota !is DoubleIota || iota.double % 1.0 != 0.0)
                throw MishapInvalidIota.of(ListIota(args.getList(0, argc)), 0, "bad_shape_list")
                else iota.double.toInt()
        }
        DataStorage.setField(env.world, name, Field.Zeros(shape))
        return listOf(FieldIota(Pair(name, shape)))
    }

}