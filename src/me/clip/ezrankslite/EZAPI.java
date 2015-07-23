package me.clip.ezrankslite;

import me.clip.ezrankslite.multipliers.CostHandler;
import me.clip.ezrankslite.rankdata.Rankup;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collection;
import java.util.Locale;

/**
 * Made by Tarkan Nielsen 2015-23-07.
 *
 * The reason this is deprecated is because it's not the supported API,
 * this API was created to keep other plugin authors' plugins compatible with both
 * 1.X releases of the plugin and 2.X releases of the plugin.
 *
 * Disclaimer! Not all methods were able to be recovered, due to the fact, that I
 * and probably Ryan, doesn't want to implement too many deprecated classes from
 * the older versions of EZRanksLite to 2.X.
 */
@Deprecated
public class EZAPI {
    private Rankup getCurrentRankObj(final Player player) {
        final Rankup rankup = Rankup.getRankup(player);
        if (rankup == null) {
            return Rankup.getAllRankups().lastEntry().getValue();
        }

        if (rankup.getOrder() <= 1) {
            return Rankup.getAllRankups().firstEntry().getValue();
        }

        return Rankup.getAllRankups().get(rankup.getOrder());
    }

    private String formatNumber(double number) {
        return new DecimalFormat("#,###.##", new DecimalFormatSymbols(Locale.US)).format(new BigDecimal(number));
    }

    @Deprecated
    public String[] getServerGroups() {
        return EZRanksLite.get().getPerms().getServerGroups();
    }

    @Deprecated
    public String getCurrentRank(final Player player) {
        return getCurrentRankObj(player).getRank();
    }

    @Deprecated
    public String[] getPermissionsGroups(final Player player) {
        return EZRanksLite.get().getPerms().getGroups(player);
    }

    @Deprecated
    public double getEconBalance(final Player player) {
        return EZRanksLite.get().getEconomy().getBalance(player);
    }

    @Deprecated
    public String getFormattedBalance(final Player player) {
        return formatNumber(getEconBalance(player));
    }

    @Deprecated
    public String getRankupName(final Player player) {
        return getCurrentRankObj(player).getRankup();
    }

    @Deprecated
    public String getRankupPrefix(final Player player) {
        final Rankup rankup = Rankup.getRankup(player);
        return rankup == null ? Rankup.getLastRank().getPrefix() : rankup.getPrefix();
    }

    @Deprecated
    public double getRankupCost(final Player player) {
        final Rankup rankup = Rankup.getRankup(player);
        if (rankup == null) {
            return 0.0;
        } else {
            return rankup.getCost();
        }
    }

    @Deprecated
    public String getRankupCostFormatted(final Player player) {
        return formatNumber(getRankupCost(player));
    }

    @Deprecated
    public int getRankupProgress(final Player player) {
        final Rankup rankup = Rankup.getRankup(player);
        if (rankup == null) return 100;

        double cost = rankup.getCost();
        cost = CostHandler.getMultiplier(player, cost);
        cost = CostHandler.getDiscount(player, cost);

        return getProgress(getEconBalance(player), cost);
    }

    @Deprecated
    public int getProgress(final double balance, final double cost) {
        double percent = balance * 100.0f / cost;
        int progress = (int) Math.floor(percent);

        if (progress > 100 || progress < 0) {
            return progress > 100 ? 100 : 0;
        } else {
            return progress;
        }
    }

    @Deprecated
    public String getRankupProgressBar(final Player player) {
        return null;
    }

    @Deprecated
    public String getProgressBar(final int progress) {
        String endColor = ChatColor.WHITE.toString() + ChatColor.BOLD;
        String barColor = ChatColor.GREEN.toString();
        String l = "┃";
        String b = ":";
        String r = "┃";
        String needColor = ChatColor.DARK_GRAY.toString();

        if (progress >= 10 && progress <= 19) return endColor + l + barColor + b + needColor + b + b + b + b + b + b + b + b + endColor + r;
        else if (progress >= 20 && progress <= 29) return endColor + l + barColor + b + b + needColor + b + b + b + b + b + b + b + endColor + r;
        else if (progress >= 30 && progress <= 39) return endColor + l + barColor + b + b + b + needColor + b + b + b + b + b + b + endColor + r;
        else if (progress >= 40 && progress <= 49) return endColor + l + barColor + b + b + b + b + needColor + b + b + b + b + b + endColor + r;
        else if (progress >= 50 && progress <= 59) return endColor + l + barColor + b + b + b + b + b + needColor + b + b + b + b + endColor + r;
        else if (progress >= 60 && progress <= 69) return endColor + l + barColor + b + b + b + b + b + b + needColor + b + b + b + endColor + r;
        else if (progress >= 70 && progress <= 79) return endColor + l + barColor + b + b + b + b + b + b + b + needColor + b + b + endColor + r;
        else if (progress >= 80 && progress <= 89) return endColor + l + barColor + b + b + b + b + b + b + b + b + needColor + b + endColor + r;
        else if (progress >= 90 && progress <= 99) return endColor + l + barColor + b + b + b + b + b + b + b + b + b + endColor + r;
        else if (progress >= 100) return Rankup.getLastRank().getRank();
        else return endColor + l + needColor + b + b + b + b + b + b + b + b + b + endColor + r;
    }

    @Deprecated
    public Collection<Object> getRankups(final Player player) {
        throw new UnsupportedOperationException("getRankups(Player) is no longer supported!");
    }

    @Deprecated
    public Object getNextRankup(final Player player) {
        throw new UnsupportedOperationException("getNextRankup(Player) is no longer supported!");
    }

    @Deprecated
    public Object getRankData(final Player player) {
        throw new UnsupportedOperationException("getNextRankup(Player) is no longer supported!");
    }

    @Deprecated
    public Object getRankData(final String permissionsGroup) {
        throw new UnsupportedOperationException("getNextRankup(String) is no longer supported!");
    }

    @Deprecated
    public void setPlayerPlaceholder(final String playerName, final String identifier, final String value) {
        throw new UnsupportedOperationException("setPlayerPlaceholder(String, String, String) is no longer supported!");
    }

    @Deprecated
    public void setCustomPlaceholder(final String playerName, final String identifier, final String value) {
        throw new UnsupportedOperationException("setCustomPlaceholder(String, String, String) is no longer supported!");
    }

    @Deprecated
    public void setPlayerPlaceholder(final Player player, final String identifier, final String value) {
        throw new UnsupportedOperationException("setPlayerPlaceholder(String, String, String) is no longer supported!");
    }

    @Deprecated
    public void setGlobalPlaceholder(final String identifier, final String value) {
        throw new UnsupportedOperationException("setGlobalPlaceholder(String, String) is no longer supported!");
    }

    @Deprecated
    public void updateScoreboard(final Player player) {
        throw new UnsupportedOperationException("updateScoreboard(Player) is no longer supported!");
    }
}
