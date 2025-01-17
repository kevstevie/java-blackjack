package view;

import domain.card.Card;
import domain.player.Dealer;
import domain.player.Gambler;
import domain.player.Player;
import domain.player.Players;
import domain.stake.Bet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class OutputView {

    private static final String DELIMITER = ", ";
    private static final String NAME_FORMAT = "카드: ";
    private static final String RESULT_GUIDE_MESSAGE = "## 최종 수익";
    private static final String SCORE_GUIDE_MESSAGE = " - 결과: ";
    private static final String POSTFIX_INITIAL_PICK_GUIDE_MESSAGE = "에게 2장을 나누었습니다.";
    private static final String PREFIX_INITIAL_PICK_GUIDE_MESSAGE = "딜러와 ";
    private static final String COLON = ": ";
    private static final String DEALER_DRAW_MESSAGE = "딜러는 16이하라 한장의 카드를 더 받았습니다.";
    private static final String BUSTED_RESULT_GUIDE_MESSAGE = "Busted";
    private static final int FIRST_INDEX_CARD = 0;
    private static final String BLACKJACK_GUIDE_MESSAGE = "BLACKJACK!";

    private OutputView() {
    }

    public static void printInitialPickGuideMessage(final Players players) {
        System.out.print(PREFIX_INITIAL_PICK_GUIDE_MESSAGE);
        List<String> playerNames = getPlayerNames(players);
        System.out.println(String.join(DELIMITER, playerNames) + POSTFIX_INITIAL_PICK_GUIDE_MESSAGE);
        printNewLine();
    }

    private static List<String> getPlayerNames(final Players players) {
        return players.getPlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    public static void printInitialCards(final Dealer dealer, final Players players) {
        printDealerCard(dealer);
        printInitialCards(players);
    }

    private static void printDealerCard(final Dealer dealer) {
        printGamblerName(dealer);
        Card oneCard = dealer.getCards()
                .get(FIRST_INDEX_CARD);
        System.out.println(CardParser.parse(oneCard));
    }

    private static void printGamblerName(final Gambler gambler) {
        String name = gambler.getName();
        System.out.print(name + NAME_FORMAT);
    }

    private static void printGamblerCards(final Gambler gambler) {
        List<String> output = getGamblerCards(gambler);
        System.out.print(String.join(DELIMITER, output));
    }

    private static List<String> getGamblerCards(final Gambler gambler) {
        List<String> output = new ArrayList<>();
        for (Card card : gambler.getCards()) {
            output.add(CardParser.parse(card));
        }
        return output;
    }

    private static void printInitialCards(final Players players) {
        players.getPlayers()
                .forEach(OutputView::printSinglePlayer);
    }

    public static void printSinglePlayer(final Player player) {
        printGamblerName(player);
        printGamblerCards(player);
        printNewLine();
    }

    public static void printScore(final Gambler gambler) {
        printGamblerName(gambler);
        printGamblerCards(gambler);
        System.out.print(SCORE_GUIDE_MESSAGE);
        if (isSpecialScore(gambler)) {
            return;
        }
        System.out.print(gambler.getScore().getValue());
        printNewLine();
    }

    private static boolean isSpecialScore(final Gambler gambler) {
        if (gambler.isBusted()) {
            System.out.println(BUSTED_RESULT_GUIDE_MESSAGE);
            return true;
        }
        if (gambler.isBlackjack()) {
            System.out.println(BLACKJACK_GUIDE_MESSAGE);
            return true;
        }
        return false;
    }

    public static void printDealerDrawMessage() {
        System.out.println(DEALER_DRAW_MESSAGE);
        printNewLine();
    }

    public static void printResult(final Map<Gambler, Bet> prizeResults) {
        printNewLine();
        System.out.println(RESULT_GUIDE_MESSAGE);
        prizeResults.forEach(
                (gambler, prize) -> System.out.println(gambler.getName() + COLON + prize.getValue())
        );
    }

    private static void printNewLine() {
        System.out.println();
    }
}
