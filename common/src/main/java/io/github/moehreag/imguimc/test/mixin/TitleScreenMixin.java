package io.github.moehreag.imguimc.test.mixin;

import imgui.ImGui;
import io.github.moehreag.imguimc.ImGuiMC;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {

	@Inject(method = "render", at = @At("TAIL"))
	private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci){


		ImGui.pushFont(ImGuiMC.FIRA_SANS_REGULAR);
		if(ImGui.button("BUTTTONNNN", 200 * ImGuiMC.getScaleFactor(), 20 * ImGuiMC.getScaleFactor())){
			System.out.println("Button pressed");
		}
		ImGui.textColored(0xFF00FF00, "skjnhrkdrjnhdh");
		ImGui.popFont();
	}
}
