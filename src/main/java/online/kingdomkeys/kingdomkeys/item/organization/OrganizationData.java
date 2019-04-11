package online.kingdomkeys.kingdomkeys.item.organization;

import net.minecraft.item.Item;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeLevel;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Stores the data loaded from the keyblades datapack
 */
public class OrganizationData {

    int baseStrength, baseMagic;
    //Description for the tooltip of the keyblade
    String description;

    public OrganizationData() {

    }

    public OrganizationData(String description, int baseStrength, int baseMagic) {
        this.description = description;
        this.baseStrength = baseStrength;
        this.baseMagic = baseMagic;
    }

    //Returns the base strength if level is 0
    public int getStrength() {
        return baseStrength;
    }

    //Returns the base magic if level is 0
    public int getMagic() {
        return baseMagic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBaseMagic(int baseMagic) {
        this.baseMagic = baseMagic;
    }

    public void setBaseStrength(int baseStrength) {
        this.baseStrength = baseStrength;
    }
}