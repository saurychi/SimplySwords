package net.sweenus.simplyswords.event;

import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.platform.Platform;
import net.minecraft.advancement.Advancement;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.sweenus.simplyswords.SimplySwords;

public final class FirstJoinBookHandler {
    private static final Identifier FIRST_JOIN_ADVANCEMENT =
            new Identifier(SimplySwords.MOD_ID, "grant_book_on_first_join");
    private static final Identifier PATCHOULI_BOOK = new Identifier("patchouli", "guide_book");

    private FirstJoinBookHandler() {
    }

    public static void init() {
        PlayerEvent.PLAYER_JOIN.register(FirstJoinBookHandler::onPlayerJoin);
    }

    private static void onPlayerJoin(ServerPlayerEntity player) {
        Advancement advancement = player.server.getAdvancementLoader().get(FIRST_JOIN_ADVANCEMENT);
        if (advancement == null || player.getAdvancementTracker().getProgress(advancement).isDone()) {
            return;
        }

        if (SimplySwords.generalConfig.giveRunicGrimoireOnFirstJoin && Platform.isModLoaded("patchouli")) {
            ItemStack book = new ItemStack(Registries.ITEM.get(PATCHOULI_BOOK));
            NbtCompound nbt = book.getOrCreateNbt();
            nbt.putString("patchouli:book", "simplyswords:runic_grimoire");
            if (!player.getInventory().insertStack(book)) {
                player.dropItem(book, false);
            }
        }

        player.getAdvancementTracker().grantCriterion(advancement, "joined");
    }
}
