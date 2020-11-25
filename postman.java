import java.util.logging.Level;
import java.util.logging.Logger;

public class postman {
    public static interface Sendable {
        String getFrom();

        String getTo();
    }
//********************************************************************************
    public static abstract class AbstractSendable implements Sendable {
        protected final String from;
        protected final String to;
        public AbstractSendable(String from, String to) {
            this.from = from;
            this.to = to;
        }
        @Override
        public String getFrom() {
            return from;
        }
        @Override
        public String getTo() {
            return to;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AbstractSendable that = (AbstractSendable) o;
            if (!from.equals(that.from)) return false;
            if (!to.equals(that.to)) return false;
            return true;
        }
    }
//*********************************************************************************************
    /*
    Письмо, у которого есть текст, который можно получить с помощью метода `getMessage`
    */
    public static class MailMessage extends AbstractSendable {

        private final String message;

        public MailMessage(String from, String to, String message) {
            super(from, to);
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            MailMessage that = (MailMessage) o;

            if (message != null ? !message.equals(that.message) : that.message != null) return false;

            return true;
        }
    }
//********************************************************************************************
    /*
    Посылка, содержимое которой можно получить с помощью метода `getContent`
    */
    public static class MailPackage extends AbstractSendable {
        private final Package content;

        public MailPackage(String from, String to, Package content) {
            super(from, to);
            this.content = content;
        }

        public Package getContent() {
            return content;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            MailPackage that = (MailPackage) o;

            if (!content.equals(that.content)) return false;

            return true;
        }
    }
//******************************************************************************************************
    /*
    Класс, который задает посылку. У посылки есть текстовое описание содержимого и целочисленная ценность.
    */
    public static class Package {
        private final String content;
        private final int price;

        public Package(String content, int price) {
            this.content = content;
            this.price = price;
        }

        public String getContent() {
            return content;
        }

        public int getPrice() {
            return price;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Package aPackage = (Package) o;

            if (price != aPackage.price) return false;
            if (!content.equals(aPackage.content)) return false;

            return true;
        }
    }
//********************************************************************************************
    /*
Интерфейс, который задает класс, который может каким-либо образом обработать почтовый объект.
*/
    public static interface MailService {
        Sendable processMail(Sendable mail);
    }
//*********************************************************************************************
    /*
    Класс, в котором скрыта логика настоящей почты
    */
    public static class RealMailService implements MailService {
    Sendable mail;
        @Override
        public Sendable processMail(Sendable mail) {
            // Здесь описан код настоящей системы отправки почты.
           this.mail= mail;
            return this.mail;//this.mail;
        }
        //String getMailContent (){
        //    return  this.mail.content.content;
        //}
    }

    /*
Вам необходимо описать набор классов, каждый из которых является MailService:

1) UntrustworthyMailWorker – класс, моделирующий ненадежного работника почты, 
который вместо того, чтобы передать почтовый объект непосредственно в сервис почты, 
последовательно передает этот объект набору третьих лиц, а затем, в конце концов, 
передает получившийся объект непосредственно экземпляру RealMailService.
 У UntrustworthyMailWorker должен быть конструктор от массива MailService 
 ( результат вызова processMail первого элемента массива передается на вход processMail второго элемента, и т. д.)
  и метод getRealMailService, который возвращает ссылку на внутренний экземпляр RealMailService.

2) Spy – шпион, который логгирует о всей почтовой переписке, которая проходит через его руки. 
Объект конструируется от экземпляра Logger, с помощью которого шпион будет сообщать о всех действиях. 
Он следит только за объектами класса MailMessage и пишет в логгер следующие сообщения 
(в выражениях нужно заменить части в фигурных скобках на значения полей почты):

2.1) Если в качестве отправителя или получателя указан "Austin Powers", то нужно написать в лог сообщение 
с уровнем WARN: Detected target mail correspondence: from {from} to {to} "{message}"

2.2) Иначе, необходимо написать в лог сообщение с уровнем INFO: Usual correspondence: from {from} to {to}

3) Thief – вор, который ворует самые ценные посылки и игнорирует все остальное. Вор принимает в конструкторе 
переменную int – минимальную стоимость посылки, которую он будет воровать. 
Также, в данном классе должен присутствовать метод getStolenValue, который возвращает 
суммарную стоимость всех посылок, которые он своровал. Воровство происходит следующим образом: вместо 
посылки, которая пришла вору, он отдает новую, такую же, только с 
нулевой ценностью и содержимым посылки "stones instead of {content}".

4) Inspector – Инспектор, который следит за запрещенными и украденными посылками и 
бьет тревогу в виде исключения, если была обнаружена подобная посылка. 
Если он заметил запрещенную посылку с одним из запрещенных содержимым 
("weapons" и "banned substance"), то он бросает IllegalPackageException. Если он находит 
посылку, состоящую из камней (содержит слово "stones"), то тревога прозвучит в виде 
StolenPackageException. Оба исключения вы должны объявить самостоятельно в виде непроверяемых исключений.

Все классы должны быть определены как публичные и статические, 
так как в процессе проверки ваш код будет подставлен во внешний класс, 
который занимается тестированием и проверкой структуры. Для удобства во внешнем классе 
объявлено несколько удобных констант и импортировано все содержимое пакета java.util.logging. 
Для определения, посылкой или письмом является Sendable объект воспользуйтесь оператором instanceof.





    */

    public static final String AUSTIN_POWERS = "Austin Powers";
    public static final String WEAPONS = "weapons";
    public static final String BANNED_SUBSTANCE = "banned substance";
//*******************************************************************************************
    public static class UntrustworthyMailWorker implements MailService {
       private  MailService[]varWorker;
       private  Sendable mail;
       private   RealMailService realMailService;
        UntrustworthyMailWorker(MailService[] varWorkes){
        this.varWorker=varWorkes;
        realMailService = new RealMailService();
        }
        public RealMailService getRealMailService(){
            return  realMailService;
        }
        @Override
        public Sendable processMail(Sendable mail) {
           for (MailService worker : varWorker) {
               mail = worker.processMail(mail);
           }
           this.mail=mail;
            realMailService.processMail(this.mail);
            return this.mail;
        }
    }
//**********************************************************************************************
    public static class Spy implements MailService {
        Logger LOGGER;
        public Spy(Logger log){
            this.LOGGER=log;
        }
        @Override
        public Sendable processMail(Sendable mail) {
            if(!(mail instanceof MailMessage))return mail;
            if(((MailMessage) mail).from==AUSTIN_POWERS||((MailMessage) mail).to==AUSTIN_POWERS) {
                LOGGER.log(Level.WARNING,"Detected target mail correspondence: from"+((MailMessage) mail).from+
                        " to "+((MailMessage) mail).to+"\""+((MailMessage) mail).message+"\"");
            } else {
                LOGGER.log(Level.INFO,"Usual correspondence: from  "+((MailMessage) mail).from+
                        " to "+((MailMessage) mail).to);
            }
            return mail;
        }
    }
//**************************************************************************************************
    public static class Thief implements MailService {
        private  int min;
        private int stoneVaule;
        private MailPackage mail;
        public Thief(int stoneVaule) {

            this.min = stoneVaule;
        }
        @Override
        public Sendable processMail(Sendable mail) {
            if(!(mail instanceof MailPackage))return mail;
            if (((MailPackage) mail).content.price<min)return mail;
            stoneVaule +=((MailPackage) mail).content.price;
            Package p =new Package("stones instead of "+((MailPackage) mail).content.content,0) ;
            this.mail=new MailPackage(((MailPackage) mail).from,((MailPackage) mail).to,p);
            return this.mail;
        }
        int getStolenValue() {
            return stoneVaule;
        }
    }
//*********************************************************************************
    public static class IllegalPackageException extends RuntimeException {}
    public static class StolenPackageException extends RuntimeException {}
//*****************************************************************************************
    public static class Inspector implements MailService {
        @Override
        public Sendable processMail(Sendable mail) {
            if (!(mail instanceof MailPackage)) return mail;
            Package pac = ((MailPackage) mail).getContent();
            String content = pac.getContent();
            if (content.indexOf("stones instead of ") == 0) {
                throw new StolenPackageException();
            } else if (content.equals(WEAPONS) || content.equals(BANNED_SUBSTANCE)) {
                throw new IllegalPackageException();
            }


            return mail;
        }
    }
//***********************************************************************************************
    public static void main(String[] args) {
        Logger logger = Logger.getLogger("main");

        Inspector inspector = new Inspector();
        Spy spy = new Spy(logger);
        Thief thief = new Thief(100);
        MailService variousWorkers[] = {
                spy,
                thief,
                inspector
        };
        UntrustworthyMailWorker worker = new UntrustworthyMailWorker(variousWorkers);

        AbstractSendable correspondence[] = {
                new MailMessage("dog", "cat", "Hi!"),
                new MailMessage("cat", "dog", "Hi! What do you want?"),
                new MailMessage("cat", AUSTIN_POWERS, "Dog is writing me again. Can you do something?"),
                new MailMessage(AUSTIN_POWERS, "cat", "I'll do my best."),
                new MailPackage(AUSTIN_POWERS, "dog", new Package("bomb", 300)),
                new MailMessage("dog", AUSTIN_POWERS, "Why do you send me stones?"),
                new MailPackage(AUSTIN_POWERS, "dog", new Package("banned substance", 99)),
                new MailPackage(AUSTIN_POWERS, "dog", new Package("tiny bomb", 90)),
                new MailMessage(AUSTIN_POWERS, "cat", "Done."),
        };
        for (AbstractSendable sendable : correspondence) {
            try {
                worker.processMail(sendable);
            } catch (StolenPackageException e) {
                logger.log(Level.WARNING, "Inspector found stolen package: " + e);
            } catch (IllegalPackageException e) {
                logger.log(Level.WARNING, "Inspector found illegal package: " + e);
            }
        }
    }

}
