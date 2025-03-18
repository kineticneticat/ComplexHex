package dev.kineticcat.complexhex.casting.actions.mathematics.longs

import at.petrak.hexcasting.api.HexAPI
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.castables.SpecialHandler
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import at.petrak.hexcasting.api.utils.darkPurple
import dev.kineticcat.complexhex.api.asActionResult
import dev.kineticcat.complexhex.casting.ComplexHexSpecialHandlers
import net.minecraft.network.chat.Component

class SpecialHandlerLongLiteral(val l: Long) : SpecialHandler {
    override fun act() = InnerAction(l)

    override fun getName(): Component {
        val key = at.petrak.hexcasting.xplat.IXplatAbstractions.INSTANCE.specialHandlerRegistry
            .getResourceKey(ComplexHexSpecialHandlers.LONG).get()
        return HexAPI.instance().getSpecialHandlerI18nKey(key)
            .asTranslatedComponent(l).darkPurple
    }
    class InnerAction(val l: Long) : ConstMediaAction {
        override val argc = 0
        override fun execute(args: List<Iota>, env: CastingEnvironment) = l.asActionResult
    }

    class Factory : SpecialHandler.Factory<SpecialHandlerLongLiteral> {
        override fun tryMatch(pattern: HexPattern, env: CastingEnvironment?): SpecialHandlerLongLiteral? {
            val sig = pattern.anglesSignature()
            if (sig.startsWith("awdedwaaw") || sig.startsWith("dwaqawddw")) {
                val negate = sig.startsWith("dwaqawddw");
                var acc = 0L;
                for (ch in sig.substring(9)) {
                    when (ch) {
                        'w' -> {
                            acc += 1L
                        }

                        'q' -> {
                            acc += 5L;
                        }

                        'e' -> {
                            acc += 10L
                        }

                        'a' -> {
                            acc = acc.shl(1)
                        }

                        'd' -> {
                            acc = acc.shr(1)
                        }
                        // :hammer:
                        's' -> {}
                        else -> throw IllegalStateException()
                    }
                }
                if (negate) {
                    acc = -acc;
                }
                return SpecialHandlerLongLiteral(acc);
            } else {
                return null;
            }
        }

    }
}