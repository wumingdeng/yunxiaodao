'use strict';
module.exports = function(sequelize, DataTypes) {
  var hospitals = sequelize.define('hospitals', {
    hospital_name: DataTypes.STRING,
    hospital_city: DataTypes.STRING
  },{
       timestamps: false
  }, {
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return hospitals;
};