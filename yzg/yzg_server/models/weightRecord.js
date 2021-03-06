'use strict';
module.exports = function(sequelize, DataTypes) {
  var weight_records = sequelize.define('weight_records', {
    userid: DataTypes.STRING,
    week: DataTypes.INTEGER,
    recordDate: DataTypes.INTEGER,
    hospital: DataTypes.STRING,
    weight:DataTypes.FLOAT,
    result: DataTypes.STRING,
    standard:DataTypes.STRING,
    tip:DataTypes.STRING,
    diet:DataTypes.STRING
  },{
       timestamps: false
  }, {
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return weight_records;
};