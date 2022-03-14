class Difficulty {
  static Random = new Difficulty("Random");
  static Easy = new Difficulty("Easy");
  static Medium = new Difficulty("Medium");

  static all = [
    this.Random,
    this.Easy,
    this.Medium
  ]

  constructor(name) {
    this.name = name
  }
}

export default Difficulty;