package io.github.moehreag.imguimc.mixin.lwjgl3;

import io.github.moehreag.imguimc.ImGuiMC;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Inject(method = "onInitFinished", at = @At("HEAD"))
	private void imgui$init(CallbackInfo ci){
		ImGuiMC.initWindow();
	}

	@Inject(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;renderTime:J"))
	private void imgui$endFrame(boolean tick, CallbackInfo ci){
		ImGuiMC.imGuiRender();
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;enableCull()V", remap = false))
	private void imgui$startFrame(boolean tick, CallbackInfo ci){
		ImGuiMC.imGuiStartFrame();
	}

	@Inject(method = "stop", at = @At("HEAD"))
	private void imgui$stop(CallbackInfo ci){
		ImGuiMC.imGuiShutdown();
	}
}
