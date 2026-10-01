package com.mrlion303.hidehud;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod("hidehud")
public class HideHudForge {
    public static final String MODID = "hidehud";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(MODID, "main"),
        () -> "1", s -> true, s -> true
    );

    public HideHudForge() {
        CHANNEL.registerMessage(0, HudPacket.class, HudPacket::encode, HudPacket::decode, HudPacket::handle);
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
        DistExecutor.unsafeRunWhenOn(net.minecraftforge.api.distmarker.Dist.CLIENT,
            () -> () -> MinecraftForge.EVENT_BUS.register(new HideHudForgeClient()));
    }

    private void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
            Commands.literal("hud").requires(s -> s.hasPermission(2))
                .then(Commands.literal("hide")
                    .then(Commands.argument("targets", EntityArgument.entities())
                        .executes(c -> send(getPlayers(c, "targets"), true, 0, false))
                        .then(Commands.argument("seconds", IntegerArgumentType.integer(0, 3600))
                            .executes(c -> send(getPlayers(c, "targets"), true,
                                IntegerArgumentType.getInteger(c, "seconds"), false))
                            .then(Commands.argument("hand", BoolArgumentType.bool())
                                .executes(c -> send(getPlayers(c, "targets"), true,
                                    IntegerArgumentType.getInteger(c, "seconds"),
                                    BoolArgumentType.getBool(c, "hand")))))))
                .then(Commands.literal("show")
                    .then(Commands.argument("targets", EntityArgument.entities())
                        .executes(c -> send(getPlayers(c, "targets"), false, 0, false))
                        .then(Commands.argument("seconds", IntegerArgumentType.integer(0, 3600))
                            .executes(c -> send(getPlayers(c, "targets"), false,
                                IntegerArgumentType.getInteger(c, "seconds"), false))))
        );
    }

    private java.util.Collection<ServerPlayer> getPlayers(
        com.mojang.brigadier.context.CommandContext<?> context, String name
    ) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        return EntityArgument.getEntities(context, name).stream()
            .filter(Entity::isAlive)
            .filter(e -> e instanceof ServerPlayer)
            .map(e -> (ServerPlayer) e)
            .toList();
    }

    private int send(java.util.Collection<ServerPlayer> players, boolean hide, int seconds, boolean hand) {
        for (ServerPlayer player : players) {
            CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new HudPacket(hide, seconds, hand));
        }
        return players.size();
    }

    public record HudPacket(boolean hide, int seconds, boolean hand) {
        public static void encode(HudPacket p, FriendlyByteBuf b) {
            b.writeBoolean(p.hide).writeVarInt(p.seconds).writeBoolean(p.hand);
        }

        public static HudPacket decode(FriendlyByteBuf b) {
            return new HudPacket(b.readBoolean(), b.readVarInt(), b.readBoolean());
        }

        public static void handle(HudPacket p, java.util.function.Supplier<net.minecraftforge.network.NetworkEvent.Context> ctx) {
            var c = ctx.get();
            c.enqueueWork(() -> {
                if (net.minecraftforge.fml.loading.FMLEnvironment.dist.isClient()) {
                    HideHudState.set(p.hide, p.seconds, p.hand);
                }
            });
            c.setPacketHandled(true);
        }
    }
}
