package io.github.moehreag.imguimc;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import ihm.imgui.ImGuiImplDisplay;
import ihm.imgui.ImGuiImplGl2;
import imgui.ImFont;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImGuiStyle;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.apache.commons.io.IOUtils;

public class ImGuiMC implements ClientModInitializer {
	private static boolean initialized;
	public static boolean LWJGL2;

	private static ImGuiImplDisplay imGuiImplDisplay;
	private static ImGuiImplGl2 imGuiImplGl2;
	private static ImGuiImplGl3 imGuiImplGl3;
	private static ImGuiImplGlfw imGuiImplGlfw;

	public static ImFont FIRA_SANS_REGULAR;

	static {
		boolean is_lwjgl_2;
		try {
			Class.forName("org.lwjgl.opengl.Display");
			is_lwjgl_2 = true;
		} catch (ClassNotFoundException e) {
			is_lwjgl_2 = false;
		}
		LWJGL2 = is_lwjgl_2;
	}

	@Override
	public void onInitializeClient() {


	}

	public static void initWindow(){
		if(!initialized) {

			ImGui.createContext();

			ImGuiIO io = ImGui.getIO();
			io.setIniFilename(null);
			io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);
			io.addConfigFlags(ImGuiConfigFlags.DockingEnable);

			try {
				FIRA_SANS_REGULAR = io.getFonts().addFontFromMemoryTTF(
						IOUtils.toByteArray(
								Objects.requireNonNull(
										ImGuiMC.class.getResourceAsStream("/assets/fonts/FiraSans-Regular.ttf"))),
						getScaledFontHeight());

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			if (LWJGL2) {
				imGuiImplGl2 = new ImGuiImplGl2();
				imGuiImplGl2.init();
				imGuiImplDisplay = new ImGuiImplDisplay();
				imGuiImplDisplay.init();
			} else {
				imGuiImplGlfw = new ImGuiImplGlfw();

				imGuiImplGlfw.init(getWindowHandle(), true);
				imGuiImplGl3 = new ImGuiImplGl3();
				imGuiImplGl3.init();
			}

			ImGuiStyle style = ImGui.getStyle();
			style.setWindowBorderSize(0);
			style.setFrameBorderSize(0);
			style.setWindowRounding(5 * getScaleFactor());
			style.setWindowTitleAlign(0, 0);
			style.setFrameRounding(5 * getScaleFactor());


			initialized = true;
		}

	}

	public static void imGuiStartFrame(){
		if(initialized) {
			if (LWJGL2) {
				imGuiImplDisplay.newFrame();
				imGuiImplGl2.newFrame();
			} else {
				imGuiImplGlfw.newFrame();
			}

			ImGui.newFrame();

			ImGui.begin("Minecraft", ImGuiWindowFlags.NoTitleBar |
					ImGuiWindowFlags.NoResize |
					ImGuiWindowFlags.NoBackground |
					ImGuiWindowFlags.NoMove);
			ImGui.setWindowSize(getWindowWidth(), getWindowHeight());
			ImGui.setWindowPos(0, 0);

		}
	}

	public static void imGuiRender(){
		if(initialized) {
			ImGui.end();
			ImGui.render();

			if (LWJGL2) {
				imGuiImplGl2.renderDrawData(ImGui.getDrawData());
			} else {
				imGuiImplGl3.renderDrawData(ImGui.getDrawData());
			}
		}
	}

	public static void imGuiShutdown(){
		if (initialized) {

			initialized = false;
		}
	}

	public static float getScaleFactor(){
		if(LWJGL2){
			try {
				Class<?> window = Class.forName(FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", "net/minecraft/class_389"));

				Object windowObject = window.getConstructor(Class.forName(FabricLoader.getInstance()
						.getMappingResolver().mapClassName("intermediary", "class_1600")));
				Method getScaleFactor = window.getMethod(FabricLoader.getInstance().getMappingResolver()
						.mapMethodName("intermediary", "net/minecraft/class_389", "method_1049", "()I"));
				Object factor = getScaleFactor.invoke(windowObject);
				return (float) factor;
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException ignored) {
			}
		} else {
			try {
				Class<?> clientClass = Class.forName(FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", "net/minecraft/class_310"));
				Method getInstance = clientClass.getMethod(FabricLoader.getInstance().getMappingResolver()
						.mapMethodName("intermediary", "net/minecraft/class_310", "method_1551", "()Lnet/minecraft/class_310;"));
				Object client = getInstance.invoke(null);
				Class<?> windowClass = Class.forName(FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", "class_12041"));
				Method getWindow = windowClass.getMethod(FabricLoader.getInstance().getMappingResolver()
						.mapMethodName("intermediary", "net/minecraft/class_310", "method_22683", "()Lnet/minecraft/class_1041;"));
				Object window = getWindow.invoke(client);
				Method getScaleFactor = windowClass.getMethod(FabricLoader.getInstance().getMappingResolver()
						.mapMethodName("intermediary", "net/minecraft/class_1041", "method_4495", "()D"));
				return (float) getScaleFactor.invoke(window);
			} catch (ClassNotFoundException | NoSuchMethodException
					 | IllegalAccessException | InvocationTargetException ignored) {
			}
		}
		return 1;
	}

	private static float getWindowWidth(){

		if(LWJGL2){
			try {
				Class<?> clientClass = Class.forName(FabricLoader.getInstance().getMappingResolver()
						.mapClassName("intermediary", "net/minecraft/class_1600"));
				Method getInstance = clientClass.getMethod(FabricLoader.getInstance().getMappingResolver()
						.mapMethodName("intermediary", "net/minecraft/class_1600", "method_2965", "()Lnet/minecraft/class_1600"));
				Object client = getInstance.invoke(null);
				Field width = clientClass.getField(FabricLoader.getInstance()
						.getMappingResolver().mapFieldName("intermediary",
								"net/minecraft/client/class_1600", "3801", "I"));
				return (float) width.get(client);
			} catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException |
					 InvocationTargetException ignored) {
			}
		} else {
			try {
				Class<?> clientClass = Class.forName(FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", "net/minecraft/class_310"));
				Method getInstance = clientClass.getMethod(FabricLoader.getInstance().getMappingResolver()
						.mapMethodName("intermediary", "net/minecraft/class_310", "method_1551", "()Lnet/minecraft/class_310;"));
				Object client = getInstance.invoke(null);
				Class<?> windowClass = Class.forName(FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", "class_12041"));
				Method getWindow = windowClass.getMethod(FabricLoader.getInstance().getMappingResolver()
						.mapMethodName("intermediary", "net/minecraft/class_310", "method_22683", "()Lnet/minecraft/class_1041;"));
				Object window = getWindow.invoke(client);
				Method getWidth = windowClass.getMethod(FabricLoader.getInstance().getMappingResolver()
						.mapMethodName("intermediary", "net/minecraft/class_1041", "method_4480", "()I"));
				return (float) getWidth.invoke(window);
			} catch (ClassNotFoundException | NoSuchMethodException
					 | IllegalAccessException | InvocationTargetException ignored) {
			}
		}
		return 800;
	}

	private static float getWindowHeight(){
		if(LWJGL2){
			try {
				Class<?> clientClass = Class.forName(FabricLoader.getInstance().getMappingResolver()
						.mapClassName("intermediary", "net/minecraft/class_1600"));
				Method getInstance = clientClass.getMethod(FabricLoader.getInstance().getMappingResolver()
						.mapMethodName("intermediary", "net/minecraft/class_1600", "method_2965", "()Lnet/minecraft/class_1600"));
				Object client = getInstance.invoke(null);
				Field height = clientClass.getField(FabricLoader.getInstance()
						.getMappingResolver().mapFieldName("intermediary",
								"net/minecraft/client/class_1600", "3802", "I"));
				return (float) height.get(client);
			} catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException |
					 InvocationTargetException ignored) {
			}

		} else {
			try {
				Class<?> clientClass = Class.forName(FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", "net/minecraft/class_310"));
				Method getInstance = clientClass.getMethod(FabricLoader.getInstance().getMappingResolver()
						.mapMethodName("intermediary", "net/minecraft/class_310", "method_1551", "()Lnet/minecraft/class_310;"));
				Object client = getInstance.invoke(null);
				Class<?> windowClass = Class.forName(FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", "class_12041"));
				Method getWindow = windowClass.getMethod(FabricLoader.getInstance().getMappingResolver()
						.mapMethodName("intermediary", "net/minecraft/class_310", "method_22683", "()Lnet/minecraft/class_1041;"));
				Object window = getWindow.invoke(client);
				Method getHeight = windowClass.getMethod(FabricLoader.getInstance().getMappingResolver()
						.mapMethodName("intermediary", "net/minecraft/class_1041", "method_4507", "()I"));
				return (float) getHeight.invoke(window);
			} catch (ClassNotFoundException | NoSuchMethodException
					 | IllegalAccessException | InvocationTargetException ignored) {
			}
		}
		return 480;
	}

	private static long getWindowHandle(){
		if(LWJGL2){
			throw new UnsupportedOperationException();
		}

		try {
			Class<?> clientClass = Class.forName(FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", "net/minecraft/class_310"));
			Method getInstance = clientClass.getMethod(FabricLoader.getInstance().getMappingResolver()
					.mapMethodName("intermediary", "net/minecraft/class_310", "method_1551", "()Lnet/minecraft/class_310;"));
			Object client = getInstance.invoke(null);
			Class<?> windowClass = Class.forName(FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", "class_12041"));
			Method getWindow = windowClass.getMethod(FabricLoader.getInstance().getMappingResolver()
					.mapMethodName("intermediary", "net/minecraft/class_310", "method_22683", "()Lnet/minecraft/class_1041;"));
			Object window = getWindow.invoke(client);
			Method getHandle = windowClass.getMethod(FabricLoader.getInstance().getMappingResolver()
					.mapMethodName("intermediary", "net/minecraft/class_1041", "method_4490", "()J"));
			return (long) getHandle.invoke(window);
		} catch (ClassNotFoundException | NoSuchMethodException
				 | IllegalAccessException | InvocationTargetException ignored) {
		}
		return 0;
	}

	private static int getScaledFontHeight(){
		return (int) (9 * getScaleFactor());
	}

	public static void tick(){
		if(LWJGL2) {
			imGuiImplDisplay.onMouse();
			imGuiImplDisplay.onKey();
		}
	}
}