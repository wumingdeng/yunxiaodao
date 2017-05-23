'use strict';
module.exports = function(sequelize, DataTypes) {
  var products = sequelize.define('products', {
    name: DataTypes.STRING,
    smallPic: DataTypes.STRING,
    swipePic: DataTypes.STRING,
    price:DataTypes.FLOAT,
    intro:DataTypes.STRING,
    size:DataTypes.STRING,
    color:DataTypes.STRING,
    type:DataTypes.STRING
  },{
       timestamps: false
  }, {
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return products;
};