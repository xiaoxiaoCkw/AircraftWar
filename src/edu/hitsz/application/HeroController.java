package edu.hitsz.application;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.game.BaseGame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 英雄机控制类
 * 监听鼠标，控制英雄机的移动
 *
 * @author hitsz
 */
public class HeroController {
    private BaseGame baseGame;
    private HeroAircraft heroAircraft;
    private MouseAdapter mouseAdapter;

    public HeroController(BaseGame baseGame, HeroAircraft heroAircraft){
        this.baseGame = baseGame;
        this.heroAircraft = heroAircraft;

        mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                int x = e.getX();
                int y = e.getY();
                if ( x<0 || x>Main.WINDOW_WIDTH || y<0 || y>Main.WINDOW_HEIGHT){
                    // 防止超出边界
                    return;
                }
                heroAircraft.setLocation(x, y);
            }
        };

        baseGame.addMouseListener(mouseAdapter);
        baseGame.addMouseMotionListener(mouseAdapter);
    }


}
