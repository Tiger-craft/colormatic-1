/*
 * Colormatic
 * Copyright (C) 2021-2022  Thalia Nero
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * As an additional permission, when conveying the Corresponding Source of an
 * object code form of this work, you may exclude the Corresponding Source for
 * "Minecraft" by Mojang Studios, AB.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.github.kvverti.colormatic.mixin.model;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.registry.BuiltinRegistries;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin {

    // using the built in DRM is fine because blocks aren't dynamic.
	// TODO: untested
    @Unique
	private static final BlockStateArgumentType BLOCK_STATE_PARSER = BlockStateArgumentType.blockState(CommandRegistryAccess.of(BuiltinRegistries.createWrapperLookup(), null));

    /**
     * Partially determines whether Colormatic should replace the tint on a model.
     * We parse the block state from the model ID. It is important that custom biome colormaps are
     * reloaded <em>before</em> this callback is run.
     */
    /*-TODO: can't get this mixin acceppted without exception
    @Dynamic("Model baking lambda in upload()")
    @Inject(
        method = "method_4733",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/model/ModelLoader;bake(Lnet/minecraft/util/Identifier;Lnet/minecraft/client/render/model/ModelBakeSettings;)Lnet/minecraft/client/render/model/BakedModel;"
        )
    )
    private void setModelIdContext(Identifier id, CallbackInfo info) {
        ModelIdContext.customTintCurrentModel = false;
        if(id instanceof ModelIdentifier modelId) {
            BlockState blockState;
            if(modelId.getVariant().equals("inventory")) {
                // we're using the block color providers for detecting non-custom item tinting for now
                var blockId = new Identifier(modelId.getNamespace(), modelId.getPath());
                blockState = Registries.BLOCK.get(blockId).getDefaultState();
            } else {
                var blockStateDesc = modelId.getNamespace() + ":" + modelId.getPath() + "[" + modelId.getVariant() + "]";
                try {
                    blockState = BLOCK_STATE_PARSER.parse(new StringReader(blockStateDesc)).getBlockState();
                } catch(CommandSyntaxException e) {
                    // don't custom tint block state models that aren't real blocks
                    return;
                }
            }
            // test first two criteria
            //  - Colormatic has custom colors for the block state
            //  - the block does not already have a color provider
            // note: we're calling a custom biome colors method. Re-evaluate if we combine custom biome colors
            // with provided biome colors.
            if(BiomeColormaps.isCustomColored(blockState)) {
                var colorProviders = ((BlockColorsAccessor)MinecraftClient.getInstance().getBlockColors()).getProviders();
                if(!colorProviders.containsKey(Registries.BLOCK.getRawId(blockState.getBlock()))) {
                    // tentatively set to true - further checking in JsonUnbakedModelMixin
                    ModelIdContext.customTintCurrentModel = true;
                }
            }
        }
    } */
}
