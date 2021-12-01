package online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuEquipmentButton;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.MenuItemsScreen;
import online.kingdomkeys.kingdomkeys.client.gui.organization.WeaponTreeSelectionScreen;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MenuEquipmentScreen extends MenuBackground {

    MenuBox listBox, detailsBox;
    Button back, showKeybladesButton;

    public MenuEquipmentScreen() {
        super(Strings.Gui_Menu_Items_Equipment, new Color(0,0,255));
        drawSeparately = true;
    }
    
    public MenuEquipmentScreen(boolean showingKeyblades) {
        super(Strings.Gui_Menu_Items_Equipment, new Color(0,0,255));
        drawSeparately = true;
        this.showingKeyblades = showingKeyblades;
    }

    int scrollOffset = 0;
    ArrayList<MenuEquipmentButton> totalButtons = new ArrayList<MenuEquipmentButton>();
    int maxItems;

    boolean showingKeyblades = false;
    @Override
    public void init() {
        super.init();
        buttons.clear();
        children.clear();
        totalButtons.clear();
        
        buttonWidth = ((float)width * 0.07F);
        float listBoxX = width * 0.16F;
        float boxY = height * 0.174F;
        float listBoxWidth = width * 0.452F;
        float boxHeight = height * 0.5972F;
        float detailsWidth = width * 0.2588F;
        float detailsX = listBoxX + listBoxWidth;
        listBox = new MenuBox((int) listBoxX, (int) boxY, (int) listBoxWidth, (int) boxHeight, new Color(76, 76, 76));
        detailsBox = new MenuBox((int) detailsX, (int) boxY, (int) detailsWidth, (int) boxHeight, new Color(76, 76, 76));

        int itemHeight = 14;
        maxItems = (int) (listBox.getHeight() / itemHeight)-1;
        int transformedScroll = scrollOffset * 15;
        
        float itemsX = width * 0.31F;
        float itemsY = height * 0.1907F;

        addButton(back = new MenuButton((int)buttonPosX, buttonPosY, (int)buttonWidth, new TranslationTextComponent(Strings.Gui_Menu_Back).getString(), MenuButton.ButtonType.BUTTON, b -> minecraft.displayGuiScreen(new MenuItemsScreen())));

        IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
        Map<ResourceLocation, ItemStack> keychains = playerData.getEquippedKeychains();
        List<String> shotlocks = Utils.getSortedShotlocks(playerData.getShotlockList());
        Map<Integer, ItemStack> items = playerData.getEquippedItems();
       // Map<Integer, ItemStack> accessories = playerData.getEquippedAccessories();


        AtomicInteger offset = new AtomicInteger();
        AtomicInteger hidden = new AtomicInteger(0);
        
        
        if (playerData.getAlignment() != Utils.OrgMember.NONE) {
            MenuEquipmentButton orgWeaponSlot = new MenuEquipmentButton(playerData.getEquippedWeapon(), (int) itemsX, (int) itemsY + offset.get() + itemHeight * offset.getAndIncrement() - transformedScroll, 0x555555, new WeaponTreeSelectionScreen(playerData.getAlignment()), ItemCategory.TOOL, this, Strings.Gui_Menu_Items_Equipment_Weapon, 0xAAAAAA);            
            totalButtons.add(orgWeaponSlot);
            addButton(orgWeaponSlot);

            addButton(showKeybladesButton = new MenuButton((int)buttonPosX, buttonPosY + 20, (int)45, new TranslationTextComponent("Keychains").getString(), MenuButton.ButtonType.BUTTON, b -> {showingKeyblades = !showingKeyblades; init();}));
            
            if(keychains.get(DriveForm.SYNCH_BLADE) != null && playerData.isAbilityEquipped(Strings.synchBlade) && (playerData.getAlignment() == Utils.OrgMember.NONE || playerData.getAlignment() == Utils.OrgMember.ROXAS)) {
            	MenuEquipmentButton sbSlot = new MenuEquipmentButton(keychains.get(DriveForm.SYNCH_BLADE), (int) itemsX, (int) itemsY +  (offset.get() - hidden.get()) + itemHeight * (offset.getAndIncrement() - hidden.get()) - transformedScroll, 0x3C0002, new MenuEquipmentSelectorScreen(DriveForm.SYNCH_BLADE, new Color(112, 31, 35), 0x3C0000), ItemCategory.TOOL, this, "ability.ability_synch_blade.name", 0xFE8185);
            	totalButtons.add(sbSlot);
                addButton(sbSlot);
            }
        } else {
        	showingKeyblades = true;
        }
        
        //Slot main keyblade
        if (keychains.get(DriveForm.NONE) != null) {
            MenuEquipmentButton firstSlot = new MenuEquipmentButton(keychains.get(DriveForm.NONE), (int) itemsX, (int) itemsY + offset.get() + itemHeight * offset.getAndIncrement() - transformedScroll, 0x3C0002, new MenuEquipmentSelectorScreen(DriveForm.NONE, new Color(112, 31, 35), 0x3C0000), ItemCategory.TOOL, this, Strings.Gui_Menu_Items_Equipment_Weapon, 0xFE8185);
            addButton(firstSlot);
            
            if(showingKeyblades)
                totalButtons.add(firstSlot);

            firstSlot.active = showingKeyblades;
            firstSlot.visible = showingKeyblades;
            hidden.getAndIncrement();
            
            //Synch blade
            if(keychains.get(DriveForm.SYNCH_BLADE) != null && playerData.getEquippedAbilityLevel(Strings.synchBlade)[1] > 0 && playerData.isAbilityEquipped(Strings.synchBlade) && playerData.getAlignment() != Utils.OrgMember.ROXAS) {
            	MenuEquipmentButton sbSlot = new MenuEquipmentButton(keychains.get(DriveForm.SYNCH_BLADE), (int) itemsX, (int) itemsY +  (offset.get()) + itemHeight * (offset.getAndIncrement() ) - transformedScroll, 0x3C0002, new MenuEquipmentSelectorScreen(DriveForm.SYNCH_BLADE, new Color(112, 31, 35), 0x3C0000), ItemCategory.TOOL, this, "ability.ability_synch_blade.name", 0xFE8185);
            	totalButtons.add(sbSlot);
                addButton(sbSlot);

                hidden.getAndIncrement();
            }
        }
        
        //Form keyblades
        Comparator<Map.Entry<ResourceLocation, ItemStack>> sortByFormOrder = Comparator.comparingInt(f -> ModDriveForms.registry.getValue(f.getKey()).getOrder());
        keychains.entrySet().stream().sorted(sortByFormOrder).forEachOrdered((entry) -> {
            ResourceLocation form = entry.getKey();
            ItemStack keychain = entry.getValue();
            if (!form.equals(DriveForm.NONE) && !form.equals(DriveForm.SYNCH_BLADE) && ModDriveForms.registry.getValue(form).hasKeychain()) {
            	MenuEquipmentButton button = new MenuEquipmentButton(keychain, (int) itemsX, (int) itemsY + offset.get() + itemHeight * offset.getAndIncrement() - transformedScroll, 0x003231, new MenuEquipmentSelectorScreen(form, new Color(10, 22, 22), 0x032F3C), ItemCategory.TOOL, this, ModDriveForms.registry.getValue(form).getTranslationKey(), 0x069293);
                addButton(button);

                hidden.getAndIncrement();
               
                if(showingKeyblades)
                	totalButtons.add(button);

                button.active = showingKeyblades;
                button.visible = showingKeyblades;
            }
        });
        
        if(!showingKeyblades)
        	offset.set(offset.get() - hidden.get());
                
        if (shotlocks != null) {
            MenuEquipmentButton shotlockSlot = new MenuEquipmentButton(playerData.getEquippedShotlock(), (int) itemsX, (int) itemsY + offset.get() + itemHeight * offset.getAndIncrement() - transformedScroll, 0x11FF44, new MenuShotlockSelectorScreen(new Color(17, 255, 100), 0x44FF99), ItemCategory.SHOTLOCK, this, Strings.Gui_Menu_Items_Equipment_Shotlock, 0x81FEAA);
            totalButtons.add(shotlockSlot);
            addButton(shotlockSlot);
        }
        
        if(items != null) {
        	 items.entrySet().stream().forEachOrdered((entry) -> {
                int slot = entry.getKey();
                ItemStack item = entry.getValue();
                MenuEquipmentButton potionSlot;
                if(slot == 0) {
                	potionSlot = new MenuEquipmentButton(item, (int) itemsX, (int) itemsY + offset.get() + itemHeight * offset.getAndIncrement() - transformedScroll, 0x007700, new MenuPotionSelectorScreen(slot, new Color(31, 112, 35), 0x22FF22), ItemCategory.CONSUMABLE, this, Utils.translateToLocal(Strings.Gui_Menu_Items_Equipment_Items)+" ["+items.size()+"]", 0x81FE85);
                } else {
                	potionSlot = new MenuEquipmentButton(item, (int) itemsX, (int) itemsY + offset.get() + itemHeight * offset.getAndIncrement() - transformedScroll, 0x007700, new MenuPotionSelectorScreen(slot, new Color(31, 112, 35), 0x22FF22), ItemCategory.CONSUMABLE, this);
                }
                totalButtons.add(potionSlot);
                addButton(potionSlot);
             });
        }
        
        //TODO the other slots for accesories, etc.
        

        for(MenuEquipmentButton b : totalButtons) {
        	b.visible = false;
        }
        
		for (int i = scrollOffset; i < scrollOffset + maxItems && i < totalButtons.size(); i++) {
			MenuEquipmentButton b = totalButtons.get(i);
			b.visible = true;
		}

        
       
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        drawMenuBackground(matrixStack, mouseX, mouseY, partialTicks);
        listBox.draw(matrixStack);
        detailsBox.draw(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {

    	if(delta < 0 && scrollOffset < totalButtons.size() - maxItems) {
    		scrollOffset++;
    	} else if (delta > 0 && scrollOffset > 0) {
    		scrollOffset--;
    	}
    	
    	System.out.println(scrollOffset);
    	init();
    	return super.mouseScrolled(mouseX, mouseY, delta);
    }
}