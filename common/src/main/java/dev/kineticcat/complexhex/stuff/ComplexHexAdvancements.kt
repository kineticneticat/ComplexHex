package dev.kineticcat.complexhex.stuff

import com.google.gson.JsonObject
import dev.kineticcat.complexhex.Complexhex.id
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance
import net.minecraft.advancements.critereon.ContextAwarePredicate
import net.minecraft.advancements.critereon.DeserializationContext
import net.minecraft.advancements.critereon.SimpleCriterionTrigger
import net.minecraft.server.level.ServerPlayer

object ComplexHexAdvancements {
    @JvmStatic
    fun init() {
        EMBEDDING = CriteriaTriggers.register(EmbeddingCriterion())
    }
    lateinit var EMBEDDING: EmbeddingCriterion
}

class EmbeddingCriterion : SimpleCriterionTrigger<EmbeddingCriterion.Condition>() {
    override fun getId() = ID
    override fun createInstance(
        jsonObject: JsonObject,
        contextAwarePredicate: ContextAwarePredicate,
        deserializationContext: DeserializationContext
    ) = Condition()
    fun trigger(player: ServerPlayer) = trigger(player) {true}
    class Condition : AbstractCriterionTriggerInstance(ID, ContextAwarePredicate.ANY)
    companion object {
        val ID = id("embedding")
    }
}