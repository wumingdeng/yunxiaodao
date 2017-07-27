'use strict';
module.exports = function(sequelize, DataTypes) {
  var discountCodes = sequelize.define('discountCodes', {
    code: DataTypes.INTEGER,
    price: DataTypes.INTEGER,
    status: DataTypes.INTEGER,
    expiryDate: DataTypes.STRING
  },{
       timestamps: false
  }, {
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return discountCodes;
};