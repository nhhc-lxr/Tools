package test.reflection;

public class Car extends Vehicle {
    public String brand = "宝马";
    public int price = 100000;


    public Car() {
        super.p();
        this.brand = super.brand;
    }

    public Car(String brand) {
        this.brand = brand;
    }

    public void printCar() {
        System.out.println("Car{" +
                "brand='" + brand + '\'' +
                ", price=" + price +
                '}');
    }
}
