package com.mrlion303.hidehud.fabric;

import com.mrlion303.hidehud.HideHudState;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class HideHudFabric implements ModInitializer {
    public static final ResourceLocation CHANNEL = new ResourceLocation("hidehud", "state");

    @Override public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registry, environment) -> dispatcher.register(
            Commands.literal("hud").requires(s -> s.hasPermission(2))
                .then(Commands.literal("hide").then(Commands.argument("targets", EntityArgument.players())
                    .executes(c -> send(EntityArgument.getPlayers(c, "targets"), true, 0, false))
                    .then(Commands.argument("seconds", IntegerArgumentType.integer(0, 3600))
                        .executes(c -> send(EntityArgument.getPlayers(c, "targets"), true, IntegerArgumentType.getInteger(c, "seconds"), false))
                        .then(Commands.argument("hand", BoolArgumentType.bool())
                            .executes(c -> send(EntityArgument.getPlayers(c, "targets"), true, IntegerArgumentType.getInteger(c, "seconds"), BoolArgumentType.getBool(c, "hand")))))))
                .then(Commands.literal("show").then(Commands.argument("targets", EntityArgument.players())
                    .executes(c -> send(EntityArgument.getPlayers(c, "targets"), false, 0, false))
                    .then(Commands.argument("seconds", IntegerArgumentType.integer(0, 3600))
                        .executes(c -> send(EntityArgument.getPlayers(c, "targets"), false, IntegerArgumentType.getInteger(c, "seconds"), false))
                        .then(Commands.argument("hand", BoolArgumentType.bool())
                            .executes(c -> send(EntityArgument.getPlayers(c, "targets"), false, IntegerArgumentType.getInteger(c, "seconds"), BoolArgumentType.getBool(c, "hand")))))))
        ));
    }

    private static int send(java.util.Collection<ServerPlayer> players, boolean hide, int seconds, boolean hand) {
        for (ServerPlayer player : players) {
            FriendlyByteBuf buf = new FriendlyByteBuf(io.netty.buffer.Unpooled.buffer());
            buf.writeBoolean(hide).writeVarInt(seconds).writeBoolean(hand);
            ServerPlayNetworking.send(player, CHANNEL, buf);
        }
        return players.size();
    }
}
