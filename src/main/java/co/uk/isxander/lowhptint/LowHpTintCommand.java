package co.uk.isxander.lowhptint.command;

import co.uk.isxander.lowhptint.LowHpTintMod;
import co.uk.isxander.lowhptint.LowHpTintScreen;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class LowHpTintCommand {

    public static void register(CommandDispatcher dispatcher) {
        dispatcher.register(
                literal("lowhptint")
                        .executes(context -> {
                            MinecraftClient.getInstance().send(() ->
                                    MinecraftClient.getInstance().setScreen(new LowHpTintScreen()));
                            return 1;
                        })
        );
    }
}