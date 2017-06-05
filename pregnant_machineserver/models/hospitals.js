'use strict';
module.exports = function(sequelize, DataTypes) {
  var hospitals = sequelize.define('hospitals', {
    // id:DataTypes.STRING,
    name: DataTypes.STRING,
    host: DataTypes.STRING
  },{
       timestamps: true,
       primaryKey: true
  }, {
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return hospitals;
};