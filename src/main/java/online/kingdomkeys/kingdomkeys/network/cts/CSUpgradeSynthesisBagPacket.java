package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.function.Supplier;

public class CSUpgradeSynthesisBagPacket {

	public CSUpgradeSynthesisBagPacket() {

	}

	public void encode(FriendlyByteBuf buffer) {
	}

	public static CSUpgradeSynthesisBagPacket decode(FriendlyByteBuf buffer) {
		CSUpgradeSynthesisBagPacket msg = new CSUpgradeSynthesisBagPacket();
		return msg;
	}

	public static void handle(CSUpgradeSynthesisBagPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			ItemStack stack = player.getMainHandItem();
			CompoundTag nbt = stack.getOrCreateTag();

			int cost = Utils.getBagCosts(nbt.getInt("level"));
			if(playerData.getMunny() >= cost) {
				playerData.setMunny(playerData.getMunny() - cost);
				nbt.putInt("level", nbt.getInt("level")+1);
			}
			
			
		});
		ctx.get().setPacketHandled(true);
	}

}
