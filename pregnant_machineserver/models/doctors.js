'use strict';
module.exports = function(sequelize, DataTypes) {
  var doctors = sequelize.define('doctors', {
    name: DataTypes.STRING,
    hospital_no: DataTypes.STRING
  },{
       timestamps: true,
  }, {
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return doctors;
};