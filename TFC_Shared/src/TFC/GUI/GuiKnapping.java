package TFC.GUI;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import TFC.TerraFirmaCraft;
import TFC.Containers.ContainerKnapping;
import TFC.Handlers.PacketHandler;

public class GuiKnapping extends GuiContainer
{
	public boolean[] craftingArea;
	private EntityPlayer player;
	
    public GuiKnapping(InventoryPlayer inventoryplayer,ItemStack is, World world, int x, int y, int z)
    {
        super(new ContainerKnapping(inventoryplayer, is, world, x, y, z));
        player = inventoryplayer.player;
    }

    @Override
	public void onGuiClosed()
    {
        super.onGuiClosed();
    }
    
    @Override
	public void initGui()
	{
		super.initGui();
		//guiLeft = (width - 208) / 2;
		//guiTop = (height - 198) / 2;

		buttonList.clear();
		
		for (int x = 0; x < 5; x++)
		{
			for (int y = 0; y < 5; y++)
			{
				buttonList.add(new GuiKnappingButton(x*5+y, guiLeft+(x*16)+5, guiTop + (y*16)-5, 16, 16));
				//addSlotToContainer(new SlotBlocked(craftMatrix, k1 + l * 5, 8 + k1 * 16, l * 16 - 1));
			}
		}
	}
    
    @Override
	protected void actionPerformed(GuiButton guibutton)
	{
    	((GuiKnappingButton) this.buttonList.get(guibutton.id)).drawButton = false;
    	((GuiKnappingButton) this.buttonList.get(guibutton.id)).enabled = false;
    	TerraFirmaCraft.proxy.sendCustomPacket(createUpdatePacket(guibutton.id));
    	((ContainerKnapping)player.openContainer).craftMatrix.setInventorySlotContents(guibutton.id, null);
		((ContainerKnapping)player.openContainer).onCraftMatrixChanged(((ContainerKnapping)player.openContainer).craftMatrix);
	}

    public Packet createUpdatePacket(int id)
	{
		ByteArrayOutputStream bos=new ByteArrayOutputStream(140);
		DataOutputStream dos=new DataOutputStream(bos);

		try {
			dos.writeByte(PacketHandler.Packet_Update_Knapping);
			dos.writeByte(id);
		} catch (IOException e) {
		}
		
		Packet250CustomPayload pkt=new Packet250CustomPayload();
		pkt.channel="TerraFirmaCraft";
		pkt.data = bos.toByteArray();
		pkt.length= bos.size();
		pkt.isChunkDataPacket=false;
		return pkt;
	}
    
    @Override
	protected void drawGuiContainerBackgroundLayer(float f, int p, int j)
    {
    	this.mc.renderEngine.bindTexture("/bioxx/gui_knapping.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
        int w = (width - 176) / 2;
        int h = (height - 184) / 2;
        drawTexturedModalRect(w, h, 0, 0, 175, 183);
    }
}
