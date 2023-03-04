package controlwoork;

public class Login {
    private boolean state;
    private String regMess;

    public Login(boolean status) {
        this.state = status;
        regMess = returnMess();
    }

    public String returnMess() {
        if(state){
            return "авторизоваться не удалось, неверный идентификатор или пароль";
        }else return "";
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getRegMess() {
        return regMess;
    }

    public void setRegMess(String regMess) {
        this.regMess = regMess;
    }
}