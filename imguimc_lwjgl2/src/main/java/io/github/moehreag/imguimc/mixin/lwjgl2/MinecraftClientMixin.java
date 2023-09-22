package io.github.moehreag.imguimc.mixin.lwjgl2;

import io.github.moehreag.imguimc.ImGuiMC;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Inject(method = "initializeGame", at = @At("TAIL"))
	private void imgui$init(CallbackInfo ci){
		ImGuiMC.initWindow();
	}

	@Inject(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/AchievementNotification;tick()V"))
	private void imgui$endFrame(CallbackInfo ci){
		ImGuiMC.imGuiRender();
	}

	@Inject(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ClientPlayerEntity;isInsideWall()Z"))
	private void imgui$startFrame(CallbackInfo ci){
		ImGuiMC.imGuiStartFrame();
	}

	@Inject(method = "stop", at = @At("HEAD"))
	private void imgui$stop(CallbackInfo ci){
		ImGuiMC.imGuiShutdown();
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void imgui$tick(CallbackInfo ci){
		ImGuiMC.tick();
	}
}
