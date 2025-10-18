package br.uffs.cc.jarena;

public class AgenteXaxim extends Agente
{
    private int contadorMovimentos;
    private boolean emPerigo;
    private int energiaUltimoTurno;
    private boolean acheiCogumelo;
    
    public AgenteXaxim(Integer x, Integer y, Integer energia) {
        super(x, y, energia);
        setDirecao(geraDirecaoAleatoria());
        contadorMovimentos = 0;
        emPerigo = false;
        energiaUltimoTurno = energia;
        acheiCogumelo = false;
    }
    
    public void pensa() {
        if(getEnergia() < energiaUltimoTurno - 10) {
            emPerigo = true; // Detecta quando está em batalha
        } if(getEnergia() < energiaUltimoTurno - 1) {
            acheiCogumelo = false;
        }

        energiaUltimoTurno = getEnergia();
        
        if(emPerigo && getEnergia() < 300) {
            comportamentoDefensivo();
            return;
        }
        
        // Movimento aleatório cada 30 ciclos
        if(contadorMovimentos > 30 || !podeMoverPara(getDirecao())) {
            setDirecao(geraDirecaoAleatoria());
            contadorMovimentos = 0;
            emPerigo = false;
        }
        contadorMovimentos++;

	    if(podeDividir() && getEnergia() > 1005) {
            divide();
        }
    }
    
    private void comportamentoDefensivo() {
        para();

        if(getEnergia() > 300) {
            setDirecao(geraDirecaoAleatoria());
            emPerigo = false;
        }
    }
    
    public void recebeuEnergia() {
        acheiCogumelo = true;
		para();
		enviaMensagem("ENERGIA:" + getX() + ":" + getY());
		System.out.println("Achei um cogumelo, vou ficar por aqui!");
    }
    
    public void tomouDano(int energiaRestanteInimigo) {
        // Foge se o inimigo é mais forte
        if(energiaRestanteInimigo > getEnergia()) {
            setDirecao(geraDirecaoAleatoria());
            enviaMensagem("FUJAM:" + getX() + ":" + getY());
			System.out.println("O inimigo é mais forte que eu, vou fugir!");
        }
    }
    
    public void ganhouCombate() {
        // Após vencer, continua explorando
        setDirecao(geraDirecaoAleatoria());
    }
    
    public void recebeuMensagem(String msg) {
        if(msg.startsWith("FUJAM:")) {
            String[] partes1 = msg.split(":");
            int xPerigo = Integer.parseInt(partes1[1]);
            int yPerigo = Integer.parseInt(partes1[2]);
            
            // Se está perto da área de perigo, fOGE
            if(distanciaXY(xPerigo, yPerigo) < 150) {
                setDirecao(geraDirecaoAleatoria());
            }
        }

		if(msg.startsWith("ENERGIA:")) {
            String[] partes2 = msg.split(":");
            int xEnergia = Integer.parseInt(partes2[1]);
            int yEnergia = Integer.parseInt(partes2[2]);

            if(distanciaXY(xEnergia, yEnergia) < 250 && acheiCogumelo == false) {
                int dx = xEnergia - getX();
                int dy = yEnergia - getY();

                if (Math.abs(dx) > Math.abs(dy)) {
                    // Move na horizontal
                    if (dx > 0 && podeMoverPara(DIREITA)) {
                        setDirecao(DIREITA);
                    } else if (dx < 0 && podeMoverPara(ESQUERDA)) {
                        setDirecao(ESQUERDA);
                    }
                } else {
                    // Move na vertical
                    if (dy > 0 && podeMoverPara(BAIXO)) {
                        setDirecao(BAIXO);
                    } else if (dy < 0 && podeMoverPara(CIMA)) {
                        setDirecao(CIMA);
                    }
                }
            }	
        }
    }
    
    private double distanciaXY(int x, int y) {
        int dx = getX() - x;
        int dy = getY() - y;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    public String getEquipe() {
        return "Xaxiense";
    }
}