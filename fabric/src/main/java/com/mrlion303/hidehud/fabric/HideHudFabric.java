package com.mrlion303.hidehud.fabric;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.Identifier;

public class HideHudFabric implements ModInitializer {
    public static final Identifier CHANNEL = new Identifier("hidehud", "state");

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
            dispatcher.register(
                CommandManager.literal("hud").requires(source -> source.hasPermissionLevel(2))
                    .then(CommandManager.literal("hide")
                        .then(CommandManager.argument("targets", EntityArgumentType.entities())
                            .executes(c -> send(getPlayers(c, "targets"), true, 0, false))
                            .then(CommandManager.argument("seconds", IntegerArgumentType.integer(0, 3600))
                                .executes(c -> send(getPlayers(c, "targets"), true,
                                    IntegerArgumentType.getInteger(c, "seconds"), false))
                                .then(CommandManager.argument("hand", BoolArgumentType.bool())
                                    .executes(c -> send(getPlayers(c, "targets"), true,
                                        IntegerArgumentType.getInteger(c, "seconds"),
                                        BoolArgumentType.getBool(c, "hand")))))))
                    .then(CommandManager.literal("show")
                        .then(CommandManager.argument("targets", EntityArgumentType.entities())
                            .executes(c -> send(getPlayers(c, "targets"), false, 0, false))
                            .then(CommandManager.argument("seconds", IntegerArgumentType.integer(0, 3600))
                                .executes(c -> send(getPlayers(c, "targets"), false,
                                    IntegerArgumentType.getInteger(c, "seconds"), false)))))
            )
        );
    }

    private static java.util.Collection<ServerPlayerEntity> getPlayers(
            com.mojang.brigadier.context.CommandContext<ServerCommandSource> context, String name
    ) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        return EntityArgumentType.getEntities(context, name).stream()
            .filter(Entity::isAlive)
            .filter(e -> e instanceof ServerPlayerEntity)
            .map(e -> (ServerPlayerEntity) e)
            .toList();
    }

    private static int send(java.util.Collection<ServerPlayerEntity> players,
                            boolean hide, int seconds, boolean hand) {
        for (ServerPlayerEntity player : players) {
            PacketByteBuf buf = new PacketByteBuf(io.netty.buffer.Unpooled.buffer());
            buf.writeBoolean(hide);
            buf.writeVarInt(seconds);
            buf.writeBoolean(hand);
            ServerPlayNetworking.send(player, CHANNEL, buf);
        }
        return players.size();
    }
}
