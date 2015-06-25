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

		PlaceholderAPI.registerPlaceholder("ezrl2_rank",
				new PlaceholderRequestEventHandler() {

					@Override
					public String onPlaceholderRequest(PlaceholderRequestEvent e) {

						Player p = e.getPlayer();

						Rankup r = Rankup.getRankup(p);

						return r != null && r.getRank() != null ? r.getRank()
								: Rankup.isLastRank(p) && Rankup.getLastRank() != null
										&& Rankup.getLastRank().getRank() != null ? Rankup
										.getLastRank().getRank() : EZRanksLite.get().getPerms()
										.getPrimaryGroup(p) != null ? EZRanksLite.get().getPerms()
										.getPrimaryGroup(p) : "unknown";

					}

				});

		PlaceholderAPI.registerPlaceholder("ezrl2_rankup",
				new PlaceholderRequestEventHandler() {

					@Override
					public String onPlaceholderRequest(PlaceholderRequestEvent e) {

						Player p = e.getPlayer();

						Rankup r = Rankup.getRankup(p);

						return r != null && r.getRankup() != null ? r
								.getRankup() : "none";

					}

				});
		
		PlaceholderAPI.registerPlaceholder("ezrl2_rankup_cost",
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

						long send = (long) cost;

						return String.valueOf(send);
	

					}

				});

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
		
		PlaceholderAPI.registerPlaceholder("ezrl2_difference",
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

						return String.valueOf(EcoUtil.getDifference(EZRanksLite.get().getEconomy().getBalance(p), cost));
					}

				});

		PlaceholderAPI.registerPlaceholder("ezrl2_difference_formatted",
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

						return EcoUtil.fixMoney(EcoUtil.getDifference(EZRanksLite.get().getEconomy().getBalance(p), cost));
					}

				});
		
		PlaceholderAPI.registerPlaceholder("ezrl2_progress",
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

						return String.valueOf(EcoUtil.getProgress(EZRanksLite.get().getEconomy().getBalance(p), cost));
					}

				});
		
		PlaceholderAPI.registerPlaceholder("ezrl2_progressexact",
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

						return EcoUtil.getProgressExact(EZRanksLite.get().getEconomy().getBalance(p), cost);
					}

				});
		
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
		
		PlaceholderAPI.registerPlaceholder("ezrl2_balance",
				new PlaceholderRequestEventHandler() {

					@Override
					public String onPlaceholderRequest(PlaceholderRequestEvent e) {

						Player p = e.getPlayer();

						return String.valueOf(EZRanksLite.get().getEconomy().getBalance(p));
					}

				});

		PlaceholderAPI.registerPlaceholder("ezrl2_balance_formatted",
				new PlaceholderRequestEventHandler() {

					@Override
					public String onPlaceholderRequest(PlaceholderRequestEvent e) {

						Player p = e.getPlayer();

						return EcoUtil.fixMoney(plugin.getEconomy().getBalance(p));
					}

				});
		
		PlaceholderAPI.registerPlaceholder("ezrl2_rankprefix",
				new PlaceholderRequestEventHandler() {

					@Override
					public String onPlaceholderRequest(PlaceholderRequestEvent e) {

						Player p = e.getPlayer();
						
						Rankup r = Rankup.getRankup(p);

						return r != null && r.getPrefix() != null ? r
								.getPrefix() : Rankup.isLastRank(p)
								&& Rankup.getLastRank() != null
								&& Rankup.getLastRank().getPrefix() != null ? Rankup
								.getLastRank().getPrefix() : "";
					}

				});
		
		PlaceholderAPI.registerPlaceholder("ezrl2_rankupprefix",
				new PlaceholderRequestEventHandler() {

					@Override
					public String onPlaceholderRequest(PlaceholderRequestEvent e) {

						Player p = e.getPlayer();
						
						Rankup r = Rankup.getRankup(p);

						if (r == null) {
							return "";
						}
						
						if (Rankup.getRankup(r.getRankup()) == null) {
							return Rankup.getLastRank() != null
									&& Rankup.getLastRank().getPrefix() != null ? Rankup
									.getLastRank().getPrefix() : "";
						}

						return Rankup.getRankup(r.getRankup()).getPrefix() != null ? Rankup
								.getRankup(r.getRankup()).getPrefix() : "";
					}

				});
		
		PlaceholderAPI.registerPlaceholder("ezrl2_lastrank",
				new PlaceholderRequestEventHandler() {

					@Override
					public String onPlaceholderRequest(PlaceholderRequestEvent e) {

							return Rankup.getLastRank() != null
									&& Rankup.getLastRank().getRank() != null ? Rankup
											.getLastRank().getRank() : "";
					}

				});
		
		PlaceholderAPI.registerPlaceholder("ezrl2_lastrankprefix",
				new PlaceholderRequestEventHandler() {

					@Override
					public String onPlaceholderRequest(PlaceholderRequestEvent e) {

							return Rankup.getLastRank() != null
									&& Rankup.getLastRank().getPrefix() != null ? Rankup
											.getLastRank().getPrefix() : "";
					}

				});

	
	}

}
