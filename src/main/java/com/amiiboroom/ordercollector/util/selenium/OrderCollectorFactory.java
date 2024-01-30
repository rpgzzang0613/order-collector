package com.amiiboroom.ordercollector.util.selenium;

import com.amiiboroom.ordercollector.util.enums.SiteType;
import org.openqa.selenium.WebDriver;

public class OrderCollectorFactory {

    public static OrderCollector newInstance(SiteType siteType, WebDriver driver) {
        OrderCollector orderCollector;

        switch(siteType) {
            case NAVER -> orderCollector = new NaverOrderCollector(driver);
            case SOFRANOMALL -> orderCollector = new SofranoMallOrderCollector(driver);
            case GAMEWOORI -> orderCollector = new GameWooriOrderCollector(driver);
            case DAEWONSHOP -> orderCollector = new DaewonShopOrderCollector(driver);
            case BANDAIMALL -> orderCollector = new BandaiMallOrderCollector(driver);
            case AUCTION -> orderCollector = new AuctionOrderCollector(driver);
            case GMARKET -> orderCollector = new GMarketOrderCollector(driver);
            case COUPANG -> orderCollector = new CoupangOrderCollector(driver);
            case TMON -> orderCollector = new TmonOrderCollector(driver);
            case YES24 -> orderCollector = new Yes24OrderCollector(driver);
            case COMICSART -> orderCollector = new ComicsArtOrderCollector(driver);
            case GUNDAMBOOM -> orderCollector = new GundamBoomOrderCollector(driver);
            case KYOUMASHOP -> orderCollector = new KyoumaShopOrderCollector(driver);
            case H2MALL -> orderCollector = new H2MallOrderCollector(driver);
            case AMAZON -> orderCollector = new AmazonOrderCollector(driver);
            case PLAYASIA -> orderCollector = new PlayAsiaOrderCollector(driver);
            case AMIAMI -> orderCollector = new AmiAmiOrderCollector(driver);
            case LIMITEDRUN -> orderCollector = new LimitedRunOrderCollector(driver);
            case MERCARI -> orderCollector = new MerCariOrderCollector(driver);
            case ALIEXPRESS -> orderCollector = new AliExpressOrderCollector(driver);
            default -> orderCollector = null;
        }

        return orderCollector;
    }

}
