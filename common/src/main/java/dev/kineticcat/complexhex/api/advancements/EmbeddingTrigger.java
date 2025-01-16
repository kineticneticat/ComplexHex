package dev.kineticcat.complexhex.api.advancements;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static dev.kineticcat.complexhex.Complexhex.id;

public class EmbeddingTrigger extends SimpleCriterionTrigger<EmbeddingTrigger.Instance> {
    private static final ResourceLocation ID = id("embed");

    @Override
    protected Instance createInstance(JsonObject jsonObject, ContextAwarePredicate contextAwarePredicate, DeserializationContext deserializationContext) {
        return new Instance(ID, contextAwarePredicate);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        public Instance(ResourceLocation resourceLocation, ContextAwarePredicate contextAwarePredicate) {
            super(resourceLocation, contextAwarePredicate);
        }
        @Override
        public @NotNull ResourceLocation getCriterion() { return ID; }


    }
}
