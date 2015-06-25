package me.clip.ezrankslite.hooks;

import org.bukkit.entity.Player;

import be.maximvdw.featherboard.api.PlaceholderAPI;
import be.maximvdw.featherboard.api.PlaceholderAPI.PlaceholderRequestEvent;
import be.maximvdw.featherboard.api.PlaceholderAPI.PlaceholderRequestEventHandler;
import me.clip.ezrankslite.EZRanksLite;
import me.clip.ezrankslite.multipliers.CostHandler;
import me.clip.ezrankslite.util.EcoUtil;
import me.clip.ezrankslite.rankdata.Rankup;

public class FeatherboardTempHook {
	
	private EZRanksLite plugin;
	
	public FeatherboardTempHook(EZRanksLite instance) {
		plugin = instance;
	}
	
	public void addPlaceholders() {

		plugin.getLogger().info("Adding progressbar hotfix placeholder: {ezrl2_progressbar}");
		
		PlaceholderAPI.registerPlaceholder("ezrl2_progressbar",
				new PlaceholderRequestEventHandler() {

					@Override
					public String onPlaceholderRequest(PlaceholderRequestEvent e) {

						Player p = e.getPlayer();

						Rankup r = Rankup.getRankup(p);

						double cost = 0.0;

						if (r != null) {

							cost = r.getCost();

							cost = CostHandler.getMultiplier(p, cost);

							cost = CostHandler.getDiscount(p, cost);
						}

						return EcoUtil.getProgressBar(EcoUtil.getProgressInt(plugin.getEconomy().getBalance(p), cost));
					}

				});
		
		plugin.getLogger().info("Adding balance formatted hotfix placeholder: {ezrl2_balance_formatted}");

		PlaceholderAPI.registerPlaceholder("ezrl2_balance_formatted",
				new PlaceholderRequestEventHandler() {

					@Override
					public String onPlaceholderRequest(PlaceholderRequestEvent e) {

						Player p = e.getPlayer();

						return EcoUtil.fixMoney(plugin.getEconomy().getBalance(p));
					}

				});
		
		plugin.getLogger().info("Adding rankup cost formatted hotfix placeholder: {ezrl2_rankup_cost_formatted}");

		PlaceholderAPI.registerPlaceholder("ezrl2_rankup_cost_formatted",
				new PlaceholderRequestEventHandler() {

					@Override
					public String onPlaceholderRequest(PlaceholderRequestEvent e) {

						Player p = e.getPlayer();

						Rankup r = Rankup.getRankup(p);

						double cost = 0.0;

						if (r != null) {

							cost = r.getCost();

							cost = CostHandler.getMultiplier(p, cost);

							cost = CostHandler.getDiscount(p, cost);
						}

						return EcoUtil.fixMoney(cost);
					}

				});

	}

}
